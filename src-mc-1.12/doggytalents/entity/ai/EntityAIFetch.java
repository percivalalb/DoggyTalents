package doggytalents.entity.ai;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAIFetch extends EntityAIBase {
	
    private final EntityDog dog;
    private EntityLivingBase owner;
    private EntityItem fetchableItem;
    private World world;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    private float maxDist;
    private float oldWaterCost;
    private double oldRangeSense;

    public EntityAIFetch(EntityDog dog, float maxDistIn) {
        this.dog = dog;
        this.world = dog.world;
        this.petPathfinder = dog.getNavigator();
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(this.petPathfinder instanceof PathNavigateGround))
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
    }
    
    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.dog.getOwner();
        EntityItem fetchableItem = this.getNearestFetchableItem();

        if(entitylivingbase == null)
            return false;
        else if(fetchableItem == null)
        	return false;
        else if(entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
            return false;
        else if(!this.dog.mode.isMode(EnumMode.DOCILE))
            return false;
        else if(this.dog.hasBone())
        	return false;
        else if(this.dog.isSitting())
            return false;
        else if(this.dog.isIncapacicated())
            return false;
       
        //Execute
        this.owner = entitylivingbase;
        this.fetchableItem = fetchableItem;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath() && this.isPlayingFetch() && this.dog.getDistanceSqToEntity(this.fetchableItem) > (double)(this.maxDist * this.maxDist) && !this.dog.isSitting();
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.dog.getPathPriority(PathNodeType.WATER);
        this.dog.setPathPriority(PathNodeType.WATER, 0.0F);
        this.oldRangeSense = this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.maxDist);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.fetchableItem = null;
        this.petPathfinder.clearPathEntity();
        this.dog.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }

    public EntityItem getNearestFetchableItem() {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.dog, this.dog.getEntityBoundingBox().grow(this.maxDist));
        for(Entity entity : list) {
        	if(entity instanceof EntityItem) {
        		EntityItem entityItem = (EntityItem)entity;
        		
        		if(entityItem.getItem().getItem() == ModItems.THROW_BONE && entityItem.getItem().getItemDamage() == 0)
        			return entityItem;
        			
        	}
        }
        
        return null;
    }
    
    @Override
    public void updateTask() {
    	 this.dog.getLookHelper().setLookPositionWithEntity(this.fetchableItem, 10.0F, (float)this.dog.getVerticalFaceSpeed());

         if(!this.dog.isSitting()) {
             if(--this.timeToRecalcPath <= 0) {
                 this.timeToRecalcPath = 10;

                 this.petPathfinder.tryMoveToEntityLiving(this.fetchableItem, this.dog.getAIMoveSpeed() * 9);
             }
             
             if(this.dog.getDistanceSqToEntity(this.fetchableItem) < (double)(1.5F * 1.5F) && !this.dog.hasBone()) {
              	if(this.fetchableItem.isEntityAlive()) {
              		this.fetchableItem.attackEntityFrom(DamageSource.GENERIC, 12F);
              		this.dog.setHasBone(true);
              		this.fetchableItem = null;
              	    this.dog.getNavigator().clearPathEntity();
              	}
              }
         }
    }

	public boolean isPlayingFetch() {
		return this.fetchableItem != null && this.fetchableItem.isEntityAlive();
	}
}