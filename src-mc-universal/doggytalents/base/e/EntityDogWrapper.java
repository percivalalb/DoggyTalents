package doggytalents.base.e;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * 1.11.2 Code
 */
public class EntityDogWrapper extends EntityDog {

	public EntityDogWrapper(World word) {
		super(word);
	}

	@Override
	protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
	
	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        if(!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if(target instanceof EntityDog) {
            	EntityDog entitydog = (EntityDog)target;

                if(entitydog.isTamed() && entitydog.getOwner() == owner)
                    return false;
            }
            else if(target instanceof EntityWolf) {
            	EntityWolf entitywolf = (EntityWolf)target;

                if(entitywolf.isTamed() && entitywolf.getOwner() == owner)
                    return false;
            }

            if(target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target))
                return false;
            else if(target == owner)
            	return false;
            else
                return !(target instanceof AbstractHorse) || !((AbstractHorse)target).isTame();
        }
        
        return false;
    }
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return processInteractGENERAL(player, hand) ? true : super.processInteract(player, hand);
	}
	
	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		if(this.isControllingPassengerPlayer()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.75F;
            forward = entitylivingbase.moveForward;

           if (forward <= 0.0F)
               forward *= 0.5F;

           if (this.onGround) {
               if (forward > 0.0F) {
                   float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                   float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                   this.motionX += (double)(-0.4F * f2 * 0.05F); // May change
                   this.motionZ += (double)(0.4F * f3 * 0.05F);
               }
           }
           
           this.stepHeight = 1.0F;
           this.jumpMovementFactor = this.getAIMoveSpeed() * 0.5F;

           if (this.canPassengerSteer())
           {
        	   float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.5F;

        	   this.setAIMoveSpeed(f);
        	   super.moveEntityWithHeading(strafe, forward);
           }
           else if (entitylivingbase instanceof EntityPlayer)
           {
        	   this.motionX = 0.0D;
        	   this.motionY = 0.0D;
        	   this.motionZ = 0.0D;
       		}

           this.prevLimbSwingAmount = this.limbSwingAmount;
           double d0 = this.posX - this.prevPosX;
           double d1 = this.posZ - this.prevPosZ;
           float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

           if (f4 > 1.0F)
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
}
