package doggytalents.entity.ai;

import java.util.List;

import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase
{
    private EntityDTDoggy theDog;
    private EntityLivingBase theOwner;
    World theWorld;
    private double moveSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private boolean field_75344_i;

    public EntityAIFollowOwner(EntityDTDoggy par1EntityDTDoggy, double par2, float par4, float par5)
    {
        this.theDog = par1EntityDTDoggy;
        this.theWorld = par1EntityDTDoggy.worldObj;
        this.moveSpeed = par2;
        this.petPathfinder = par1EntityDTDoggy.getNavigator();
        this.minDist = par4;
        this.maxDist = par5;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.theDog.func_130012_q();
        int order = this.theDog.masterOrder();
        
        if(entitylivingbase == null) {
        	entitylivingbase = theOwner;
        }
        if (entitylivingbase == null)
        {
            return false;
        }
        else if (this.theDog.isSitting())
        {
            return false;
        }
        else if(this.theDog.mode.isMode(EnumMode.WANDERING)) {
        	return false;
        }
        //else if(this.theDog.masterOrder() != 0) {
        //	return false;
       // }
        else if(!(this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead)) 
        {
        	return false;
        }
        else if ((this.theDog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist) || (this.theDog.riddenByEntity == null && order == 3)) && !this.theDog.hasBone() && (order == 0 || order == 3))
        {
            return false;
        }
        else
        {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() /**&& this.theDog.masterOrder() == 0**/ && (this.theDog.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist)) && !this.theDog.isSitting() && (this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.field_75343_h = 0;
        this.field_75344_i = this.theDog.getNavigator().getAvoidsWater();
        this.theDog.getNavigator().setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.theDog.getNavigator().setAvoidsWater(this.field_75344_i);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
    	int order = this.theDog.masterOrder();
    	int masterX = MathHelper.floor_double(this.theOwner.posX);
    	int masterY = MathHelper.floor_double(this.theOwner.posY);
    	int masterZ = MathHelper.floor_double(this.theOwner.posZ);
    	float distanceAway = this.theOwner.getDistanceToEntity(this.theDog);
    	
    	if((order == 0 || (order == 3 && this.theDog.riddenByEntity != null)) && distanceAway >= 2F) {
    		
	        this.theDog.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
	
	        if (!this.theDog.isSitting())
	        {
	            if (--this.field_75343_h <= 0)
	            {
	                this.field_75343_h = 10;
	
	                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed))
	                {
	                    if (!this.theDog.getLeashed())
	                    {
	                        if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 225.0D)
	                        {
	                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                            int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                            for (int l = 0; l <= 4; ++l)
	                            {
	                                for (int i1 = 0; i1 <= 4; ++i1)
	                                {
	                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
	                                    {
	                                        this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                        this.petPathfinder.clearPathEntity();
	                                        return;
	                                    }
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
    	}
    	else if(order == 1 || order == 2) {
    		int dogX = MathHelper.floor_double(this.theDog.posX);
            int dogY = MathHelper.floor_double(this.theDog.posY - 0.20000000298023224D - (double)this.theDog.yOffset);
            int dogZ = MathHelper.floor_double(this.theDog.posZ);
            int k2 = dogX - masterX;
            int i3 = dogZ - masterZ;
            int j3 = masterX + k2 * 2;
            int k3 = masterZ + i3 * 2;
            int l3 = masterX + k2 / 2;
            int i4 = masterZ + i3 / 2;
            if (distanceAway < 5F)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(j3, dogY, k3, this.moveSpeed)) {
            		 if (!this.theDog.getLeashed()) {
	                    if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
	                                {
	                                    this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                    this.petPathfinder.clearPathEntity();
	                                    return;
	                                }
	                            }
	                        }
	                    }
	                }
            	}
            }

            if (distanceAway > 12F && distanceAway < 20F)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(l3, dogY, i4, this.moveSpeed)) {
	            	if (!this.theDog.getLeashed()) {
	                    if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
	                                {
	                                    this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                    this.petPathfinder.clearPathEntity();
	                                    return;
	                                }
	                            }
	                        }
	                    }
	                }
            	}
            }
    	}
    	else if(order == 3 && this.theDog.riddenByEntity == null && this.theDog.talents.getTalentLevel(EnumTalents.SHEPHERDDOG) > 0) {
    		List list1 = this.theDog.worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(this.theDog.posX, this.theDog.posY, this.theDog.posZ, this.theDog.posX + 1.0D, this.theDog.posY + 1.0D, this.theDog.posZ + 1.0D).expand(10D, 2D, 10D));

            if (!list1.isEmpty())
            {
                EntityAnimal entityanimal = null;
                boolean flag = false;

                for (int l2 = 0; l2 < list1.size() && entityanimal == null; l2++)
                {
                    boolean flag1 = false;
                    EntityAnimal entityanimal1 = (EntityAnimal)list1.get(l2);

                    if (((entityanimal1 instanceof EntityPig) || (entityanimal1 instanceof EntityChicken) || (entityanimal1 instanceof EntityCow) || (entityanimal1 instanceof EntitySheep)))
                    {
                        flag1 = true;
                    }

                    if (flag1 && entityanimal1.ridingEntity == null)
                    {
                        entityanimal = entityanimal1;
                    }
                }

                if (entityanimal != null)
                {
                	if (entityanimal.getDistanceToEntity(this.theDog) > 2D)
                    {
                		if(!this.petPathfinder.tryMoveToEntityLiving(entityanimal, this.moveSpeed)) {
                			if (!this.theDog.getLeashed()) {
             
             	            }
                         }
                    }
                	else {
                		entityanimal.mountEntity(this.theDog);
                	}
                }
            }
    	}
    }
}
