package doggytalents.entity.ai;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.api.inferface.IWaterMovement;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.item.ItemThrowBone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAIFetch extends EntityAIBase {
	
    private final EntityDog dog;
    private EntityLivingBase owner;
    private EntityItem fetchableItem;
    private World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    private float maxDist;
    private double oldRangeSense;
    private IWaterMovement waterMovement;

    public EntityAIFetch(EntityDog dogIn, double followSpeedIn,  float maxDistIn) {
        this.dog = dogIn;
        this.world = dogIn.world;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = dogIn.getNavigator();
        this.maxDist = maxDistIn;
        this.waterMovement = new WaterMovementHandler(this.dog);
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
        else if(!this.dog.MODE.isMode(EnumMode.DOCILE))
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
        return !this.petPathfinder.noPath() && this.isPlayingFetch() && this.dog.getDistanceSq(this.fetchableItem) < (double)(this.maxDist * this.maxDist) && !this.dog.isSitting();
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.waterMovement.startExecuting();
        this.oldRangeSense = this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
        this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.maxDist);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.fetchableItem = null;
        this.petPathfinder.clearPath();
        this.waterMovement.resetTask();
        this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }

    public EntityItem getNearestFetchableItem() {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.dog, this.dog.getBoundingBox().grow(this.maxDist, this.maxDist, this.maxDist));
        for(Entity entity : list) {
        	if(entity instanceof EntityItem) {
        		EntityItem entityItem = (EntityItem)entity;
        		Item item = entityItem.getItem().getItem();
        		if(item instanceof ItemThrowBone && ((ItemThrowBone)item).type == ItemThrowBone.Type.DRY)
        			return entityItem;
        			
        	}
        }
        
        return null;
    }
    
    @Override
    public void tick() {
    	 this.dog.getLookHelper().setLookPositionWithEntity(this.fetchableItem, 10.0F, (float)this.dog.getVerticalFaceSpeed());

         if(!this.dog.isSitting()) {
             if(--this.timeToRecalcPath <= 0) {
                 this.timeToRecalcPath = 10;

                 this.petPathfinder.tryMoveToEntityLiving(this.fetchableItem, this.followSpeed);
             }
             
             if(this.dog.getDistanceSq(this.fetchableItem) < (double)(1.5F * 1.5F) && !this.dog.hasBone()) {
              	if(this.fetchableItem.isAlive()) {
              		this.fetchableItem.attackEntityFrom(DamageSource.GENERIC, 12F);
              		EntityItem entityItem = (EntityItem)this.fetchableItem;
            		Item item = entityItem.getItem().getItem();
              		
            		if(item == ModItems.THROW_BONE)
            			this.dog.setBoneVariant(0);
            		else if(item == ModItems.THROW_STICK)
            			this.dog.setBoneVariant(1);
            		
              		this.fetchableItem = null;
              	    this.dog.getNavigator().clearPath();
              	}
              }
         }
    }

	public boolean isPlayingFetch() {
		return this.fetchableItem != null && this.fetchableItem.isAlive();
	}
}