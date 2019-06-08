package doggytalents.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import doggytalents.ModItems;
import doggytalents.ModTalents;
import doggytalents.api.inferface.IWaterMovement;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIShepherdDog extends Goal {
	
	protected final EntityDog dog;
	private World world;
	
	private LivingEntity owner;
	private final Predicate<AnimalEntity> predicate;
	private final Comparator<Entity> sorter;
	protected List<AnimalEntity> targets;
    private final double followSpeed;
    private final PathNavigator dogPathfinder;
    private int timeToRecalcPath;
    private float maxDist;
    private IWaterMovement waterMovement;
    private int MAX_FOLLOW = 5;

	public EntityAIShepherdDog(EntityDog dogIn, double speedIn, float range, @Nullable Predicate<AnimalEntity> targetSelector) {
		this.dog = dogIn;
		this.world = dogIn.world;
		this.dogPathfinder = dogIn.getNavigator();
		this.followSpeed = speedIn;
		this.maxDist = range;
		this.predicate = (entity) -> {
			double d0 = this.getFollowRange();
			if(entity.isInvisible()) {
				return false;
			}
			else if (targetSelector != null && !targetSelector.test(entity)) {
	            return false;
	        } else {
				return (double)entity.getDistance(this.dog) > d0 ? false : entity.canEntityBeSeen(this.dog);
			}
		};
		this.sorter = new DogUtil.Sorter(dogIn);
		this.waterMovement = new WaterMovementHandler(this.dog);
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if(!this.dog.MODE.isMode(EnumMode.DOCILE)) {
			return false;
		} else if(this.dog.TALENTS.getLevel(ModTalents.SHEPHERD_DOG) <= 0) {
			return false;
		} else {
			LivingEntity entitylivingbase = this.dog.getOwner();
		    if(entitylivingbase == null) {
		       return false;
		    } else if(entitylivingbase instanceof PlayerEntity && ((PlayerEntity)entitylivingbase).isSpectator()) {
		    	return false;
		    } else if(!DogUtil.isHolding(entitylivingbase, ModItems.WHISTLE, nbt -> nbt.contains("mode") && nbt.getInt("mode") == 4)) {
				return false;
		    } else {
				List<AnimalEntity> list = this.world.getEntitiesWithinAABB(AnimalEntity.class, this.dog.getBoundingBox().grow(12D, 4.0D, 12D), this.predicate);
				Collections.sort(list, this.sorter);
				if(list.isEmpty()) {
					return false;
				} 
				else {
					this.MAX_FOLLOW = ModTalents.SHEPHERD_DOG.getMaxFollowers(this.dog);
					this.targets = list.subList(0, Math.min(MAX_FOLLOW, list.size()));
					this.owner = entitylivingbase;
					return true;
				}
		    }
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(!this.dog.MODE.isMode(EnumMode.DOCILE)) {
			return false;
		} else if(this.dog.TALENTS.getLevel(ModTalents.SHEPHERD_DOG) <= 0) {
			return false;
		} else if(!DogUtil.isHolding(this.owner, ModItems.WHISTLE, nbt -> nbt.contains("mode") && nbt.getInt("mode") == 4)) {
			return false;
		} else if(this.targets.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.timeToRecalcPath = 0;
		this.waterMovement.startExecuting();
	}

	@Override
	public void tick() {
		if(!this.dog.isSitting()) {
			
			if(--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = 10;

				// Pick up more animals
				if(this.targets.size() < MAX_FOLLOW) {
					List<AnimalEntity> list = this.world.getEntitiesWithinAABB(AnimalEntity.class, this.dog.getBoundingBox().grow(16, 4.0D, 16), this.predicate);
					list.removeAll(this.targets);
					Collections.sort(list, this.sorter);

					this.targets.addAll(list.subList(0, Math.min(MAX_FOLLOW - this.targets.size(), list.size())));
				}
				
				Collections.sort(this.targets, this.sorter);
				boolean teleport = this.owner.getDistance(this.targets.get(0)) > 16;
				
				for(AnimalEntity target : this.targets) {
			    	double distanceAway = target.getDistance(this.owner);
					target.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)target.getVerticalFaceSpeed());
					if(teleport) {
						if(!target.getLeashed() && !target.isPassenger())
							DogUtil.teleportDogToOwner(this.owner, target, this.world, target.getNavigator(), 4);
					}
					else if(distanceAway >= 5) {
						if(!target.getNavigator().tryMoveToEntityLiving(this.owner, 1.2D))
							if(!target.getLeashed() && !target.isPassenger() && distanceAway >= 20)
								DogUtil.teleportDogToOwner(this.owner, target, this.world, target.getNavigator(), 4);
					}
					else
						target.getNavigator().clearPath();
				}
				
				Vec3d vec = Vec3d.ZERO;
				
				// Calculate average pos of targets
				for(AnimalEntity target : this.targets) {
					vec = vec.add(target.getPositionVector());
				}
				
				vec = vec.scale(1D / this.targets.size());
				
				double dPosX = vec.x - this.owner.posX;
				double dPosZ = vec.z - this.owner.posZ;
				double size = Math.sqrt(dPosX * dPosX + dPosZ * dPosZ);
	        	double j3 = vec.x + dPosX / size * (2 + this.targets.size() / 16);
	          	double k3 = vec.z + dPosZ / size * (2 + this.targets.size() / 16);
				
	          	
				if(teleport) {
					DogUtil.teleportDogToPos(j3, this.dog.posY, k3, this.dog, this.world, this.dogPathfinder, 1);
				}
	          	
				this.dog.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.dog.getVerticalFaceSpeed());
				if(!this.dogPathfinder.tryMoveToXYZ(j3, this.owner.getBoundingBox().minY, k3, this.followSpeed)) {
					if(this.dog.getDistanceSq(j3, this.owner.getBoundingBox().minY, k3) > 144D) {
						if(!this.dog.getLeashed() && !this.dog.isPassenger())
							DogUtil.teleportDogToPos(j3, this.dog.posY, k3, this.dog, this.world, this.dogPathfinder, 4);
					}
				}
				
				if(this.dog.getDistance(this.owner) > 40)
					DogUtil.teleportDogToOwner(this.owner, this.dog, this.world, this.dogPathfinder, 2);
				// Play woof sound
				if(this.dog.getRandom().nextFloat() < 0.15F)
					this.dog.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, this.dog.getSoundVolume() + 1.0F, (this.dog.getRandom().nextFloat() - this.dog.getRandom().nextFloat()) * 0.1F + 0.9F);
				
				
				// Remove dead or faraway entities
				List<AnimalEntity> toRemove = Lists.newArrayList();
				for(AnimalEntity target : this.targets) {
					if(!target.isAlive() || target.getDistance(this.dog) > 25D)
						toRemove.add(target);
				}
				this.targets.removeAll(toRemove);
			}
	    }
	}
	
	@Override
	public void resetTask() {
		this.owner = null;
		for(AnimalEntity target : this.targets) {
			target.getNavigator().clearPath();
		}
        this.dogPathfinder.clearPath();
        this.waterMovement.resetTask();
	}
	
	protected double getFollowRange() {
		IAttributeInstance iattributeinstance = this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
	}
}