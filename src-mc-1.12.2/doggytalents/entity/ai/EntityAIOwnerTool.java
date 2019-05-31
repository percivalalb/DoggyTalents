package doggytalents.entity.ai;

import doggytalents.api.inferface.IWaterMovement;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIOwnerTool extends EntityAIBase {
	
    private final EntityDog dog;
    private EntityLivingBase owner;
    private World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    private float executeDistance;
    private float finishedDistance;
    private IWaterMovement waterMovement;

    public EntityAIOwnerTool(EntityDog thePetIn, double followSpeedIn, float executeDistance, float finishedDistance) {
        this.dog = thePetIn;
        this.world = thePetIn.world;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.executeDistance = executeDistance;
        this.finishedDistance = finishedDistance;
        this.waterMovement = new WaterMovementHandler(this.dog);
        this.setMutexBits(3);

        if(!(thePetIn.getNavigator() instanceof PathNavigateGround))
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.dog.getOwner();
        
        if(entitylivingbase == null) {
            return false;
        } else if(entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator()) {
            return false;
        } else if(this.dog.isSitting()) {
            return false;
        } else if(this.dog.getDistanceSq(entitylivingbase) > (double)(this.finishedDistance * this.finishedDistance)) {
    		return false;
    	} else if(!this.isCommanding(entitylivingbase)) {
    		return false;
    	} else if(this.dog.MODE.isMode(EnumMode.WANDERING)) {
    		return false;
    	} else {
        	this.owner = entitylivingbase;
        	return true;
        }
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath() && !this.dog.isSitting() && this.dog.getDistanceSq(this.owner) < (double)(this.finishedDistance * this.finishedDistance) && this.isCommanding(this.owner);
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.waterMovement.startExecuting();
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.petPathfinder.clearPath();
        this.waterMovement.resetTask();
    }

    @Override
    public void updateTask() {

        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                int masterX = MathHelper.floor(this.owner.posX);
            	int masterY = MathHelper.floor(this.owner.getEntityBoundingBox().minY);
            	int masterZ = MathHelper.floor(this.owner.posZ);
                
                int dogX = MathHelper.floor(this.dog.posX);
                int dogY = MathHelper.floor(this.dog.posY);
                int dogZ = MathHelper.floor(this.dog.posZ);
                int dPosX = dogX - masterX;
                int dPosZ = dogZ - masterZ;
                int j3 = masterX + dPosX * 2;
                int k3 = masterZ + dPosZ * 2;
    
                if(this.petPathfinder.tryMoveToXYZ(j3, dogY, k3, this.followSpeed))
                	this.dog.getLookHelper().setLookPosition(j3, dogY + 1, k3, 10.0F, (float)this.dog.getVerticalFaceSpeed());
            }
        }
    }
    
    public boolean isCommanding(EntityLivingBase entitylivingbase) {
    	ItemStack mainStack = entitylivingbase.getHeldItemMainhand();
    	
    	return mainStack.getItem() instanceof ItemSword || mainStack.getItem() instanceof ItemTool;
    }
}