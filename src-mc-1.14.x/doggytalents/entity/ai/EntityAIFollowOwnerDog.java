package doggytalents.entity.ai;

import java.util.EnumSet;

import doggytalents.api.inferface.IWaterMovement;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFollowOwnerDog extends Goal
{
    private final EntityDog dog;
    private LivingEntity owner;
    private World world;
    private final double followSpeed;
    private final PathNavigator petPathfinder;
    private int timeToRecalcPath;
    private float exeDis;
    private float finDis;
    private double oldRangeSense;
    private IWaterMovement waterMovement;

    public EntityAIFollowOwnerDog(EntityDog thePetIn, double followSpeedIn, float executeDistance, float finishedDistance) {
        this.dog = thePetIn;
        this.world = thePetIn.world;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.exeDis = executeDistance;
        this.finDis = finishedDistance;
        this.waterMovement = new WaterMovementHandler(this.dog);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

        if(!(thePetIn.getNavigator() instanceof GroundPathNavigator))
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity LivingEntity = this.dog.getOwner();
        
        if(LivingEntity == null) {
            return false;
        } else if(LivingEntity instanceof PlayerEntity && ((PlayerEntity)LivingEntity).isSpectator())
            return false;
        else if(this.dog.isSitting()) {
            return false;
        } else if(this.dog.canWander()) {
        	return false;
        } else {
        	
	    	double distSq = this.dog.getDistanceSq(LivingEntity);
	      	
	      	if(this.isCommanding(LivingEntity)) { //Holding Sword or tool
	      		if(distSq >= 16D && distSq <= 100D) {
	      			return false;
	      		}
	      	} else if(distSq < (double)(this.exeDis * this.exeDis)) {
	            return false;
	      	}
	      	
	        //Execute
	        this.owner = LivingEntity;
	        return true;
        }
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath() && this.isCommanding(this.owner) ? true : this.dog.getDistanceSq(this.owner) > (double)(this.finDis * this.finDis) && !this.dog.isSitting();
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.waterMovement.startExecuting();
        this.oldRangeSense = this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
      	this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.petPathfinder.clearPath();
        this.waterMovement.resetTask();
      	this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }

    @Override
    public void tick() {

        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                
            	double distSq = this.dog.getDistanceSq(this.owner);
                
            	if(this.isCommanding(this.owner)) { //Holding Sword or tool
            		
            		Vec3d diffVec = null;
            		
            		if(distSq < 25.0D) {
            			diffVec = this.dog.getPositionVector().scale(1.7D).subtract(this.owner.getPositionVector().scale(1.7D - 1.0D));
            		} else if(distSq > 100.0D) {
            			diffVec = this.owner.getPositionVector().add(this.dog.getPositionVector()).scale(0.5D);
            		}
            		
            		if(diffVec != null) {
         
            			if(!this.dog.getNavigator().tryMoveToXYZ(diffVec.x, this.dog.posY, diffVec.z, this.followSpeed)) {
            				if(!this.dog.getLeashed() && !this.dog.isPassenger())
            					if(distSq >= 350.0D)
            						DogUtil.teleportDogToOwner(this.owner, this.dog, this.world, this.petPathfinder);
            			}
            			else 
            				this.dog.getLookHelper().setLookPosition(diffVec.x, this.dog.posY + 1, diffVec.z, 10.0F, (float)this.dog.getVerticalFaceSpeed());

            		}
            	}
            	else {
                    this.dog.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.dog.getVerticalFaceSpeed());
	                if(!this.petPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed))
	                    if(!this.dog.getLeashed() && !this.dog.isPassenger())
	                        if(distSq >= 144.0D)
	                        	DogUtil.teleportDogToOwner(this.owner, this.dog, this.world, this.petPathfinder);
	                	
            	}
            }
        }
    }
    
    public boolean isCommanding(LivingEntity LivingEntity) {
    	ItemStack mainStack = LivingEntity.getHeldItemMainhand();
    	return mainStack.getItem() instanceof SwordItem || mainStack.getItem() instanceof ToolItem;
    }
}