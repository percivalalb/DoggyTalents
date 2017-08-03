package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase
{
    private final EntityDog dog;
    private EntityLivingBase owner;
    private World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    private float maxDist;
    private float minDist;
    private boolean preShouldAvoidWater;
    private double oldRangeSense;

    public EntityAIFollowOwner(EntityDog thePetIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.dog = thePetIn;
        this.world = thePetIn.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.dog.getOwner();
        
        if(entitylivingbase == null)
            return false;
        else if(this.dog.isSitting())
            return false;
        else if(!this.dog.mode.getMode().doesFollowOwner())
        	return false;
        else if(this.dog.aiFetchBone.isPlayingFetch())
        	return false;
        
      	int order = this.dog.masterOrder();
    	double distanceAway = this.dog.getDistanceSqToEntity(entitylivingbase);
      	
      	if(!this.dog.hasBone() && (order == 1 || order == 2)) { //Holding Sword or tool
      		if(distanceAway >= 16D && distanceAway <= 100D)
      			return false;
      	}
      	else
	      	if(!this.dog.hasBone() && this.dog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
	            return false;
      	
        //Execute
        this.owner = entitylivingbase;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.dog.getDistanceSqToEntity(this.owner) > (double)(this.maxDist * this.maxDist) && !this.dog.isSitting();
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.preShouldAvoidWater = this.dog.getNavigator().getAvoidsWater();
        this.dog.getNavigator().setAvoidsWater(false);
        this.oldRangeSense = this.dog.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
      	this.dog.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32.0D);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.petPathfinder.clearPathEntity();
        this.dog.getNavigator().setAvoidsWater(this.preShouldAvoidWater);
      	this.dog.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(this.oldRangeSense);
    }
    
    @Override
    public void updateTask() {

        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

            	int order = this.dog.masterOrder();
               	int masterX = MathHelper.floor_double(this.owner.posX);
            	int masterY = MathHelper.floor_double(this.owner.posY);
            	int masterZ = MathHelper.floor_double(this.owner.posZ);
            	double distanceAway = this.dog.getDistanceSqToEntity(this.owner);
                
            	if(((order == 0 || order == 3) && distanceAway >= 4.0D) || this.dog.hasBone()) {
                    this.dog.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.dog.getVerticalFaceSpeed());
	                if(!this.petPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed))
	                    if(!this.dog.hasBone() && !this.dog.getLeashed())
	                        if(distanceAway >= 144.0D)
	                        	DogUtil.teleportDogToOwner(this.owner, this.dog, this.world, this.petPathfinder);
	                	
            	}
            	else if(order == 1 || order == 2) { //Holding Sword or tool
            		int dogX = MathHelper.floor_double(this.dog.posX);
                    int dogY = MathHelper.floor_double(this.dog.posY);
                    int dogZ = MathHelper.floor_double(this.dog.posZ);
                    int dPosX = dogX - masterX;
                    int dPosZ = dogZ - masterZ;
                    int j3 = masterX + dPosX * 2;
                    int k3 = masterZ + dPosZ * 2;
                    int l3 = masterX + dPosX / 2;
                    int i4 = masterZ + dPosZ / 2;
                    if(distanceAway < 25.0D) {
                    	if(this.petPathfinder.tryMoveToXYZ(j3, dogY, k3, this.followSpeed))
                            this.dog.getLookHelper().setLookPosition(j3, dogY + 1, k3, 10.0F, (float)this.dog.getVerticalFaceSpeed());
                    }
                    else if(distanceAway > 100.0D) {
             
                    	if(!this.dog.getNavigator().tryMoveToXYZ(l3, dogY, i4, this.followSpeed)) {
        	            	if(!this.dog.getLeashed()) 
        	                    if(distanceAway >= 350.0D)
        	                    	DogUtil.teleportDogToOwner(this.owner, this.dog, this.world, this.petPathfinder);
                    	}
        	            else 
        	            	this.dog.getLookHelper().setLookPosition(l3, dogY + 1, i4, 10.0F, (float)this.dog.getVerticalFaceSpeed());
                    }
            	}
            }
        }
    }
}