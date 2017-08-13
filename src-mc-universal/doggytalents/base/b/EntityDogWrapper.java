package doggytalents.base.b;

import java.util.UUID;

import net.minecraft.pathfinding.PathNavigateGround;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDogWrapper extends EntityDog {

	public EntityDogWrapper(World world) {
		super(world);
		((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
	}

	@Override
	public void addAIMeleeAttack(int priority, double speedIn, boolean useLongMemory) {
		this.tasks.addTask(priority, new EntityAIAttackOnCollide(this, speedIn, useLongMemory)); 	
	}
	
	@Override
	public boolean isBeingRidden() {
		return this.riddenByEntity != null;
	}
	
	@Override
	public Entity getControllingPassenger() {
		return this.riddenByEntity;
	}
	
	@Override
	public Entity getEntityWeAreRiding() {
		return this.ridingEntity;
	}
	
	@Override
	public void dismountEntityWeAreRiding() {
		this.ridingEntity.riddenByEntity = null;
		this.ridingEntity = null;
	}
	
	@Override
	public void removeEntityRidingUs() {
		this.riddenByEntity.ridingEntity = null;
		this.riddenByEntity = null;
	}
	
	@Override
	public void setOwnerUUID(UUID uuid) {
		this.setOwnerId(uuid.toString());
	}
	
	@Override
	public UUID getOwnerUUID() {
		return UUID.fromString(this.getOwnerId());
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

	@Override
    protected String getLivingSound() {
        return (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }
	
	@Override
	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}
	
	@Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }
	
	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		if(this.isControllingPassengerPlayer()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.75F;
            forward = entitylivingbase.moveForward;

            if(forward <= 0.0F)
            	forward *= 0.5F;

            if(this.onGround) {
            	if (forward > 0.0F) {
            		float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
            		float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
            		this.motionX += (double)(-0.4F * f2 * 0.05F); // May change
            		this.motionZ += (double)(0.4F * f3 * 0.05F);
            	}
            }
           
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.3F;
            
            if(entitylivingbase instanceof EntityPlayer) {
            	float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.5F;

            	this.setAIMoveSpeed(f);
            	super.moveEntityWithHeading(strafe, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer) {
            	this.motionX = 0.0D;
          		this.motionY = 0.0D;
          		this.motionZ = 0.0D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_float((float)(d0 * d0 + d1 * d1)) * 4.0F;

            if(f4 > 1.0F)
            	f4 = 1.0F;

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
		}
		else {
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			super.moveEntityWithHeading(strafe, forward);
		}
	}

	@Override
	public boolean interact(EntityPlayer player) {
		return this.processInteractGENERAL(player, player.getHeldItem());
	}
}
