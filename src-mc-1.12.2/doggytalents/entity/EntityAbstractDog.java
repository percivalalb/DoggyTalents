package doggytalents.entity;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityAbstractDog extends EntityTameable {
	
    /** Float used to smooth the rotation of the wolf head */
    private float headRotationCourse;
    private float headRotationCourseOld;
    /** true is the wolf is wet else false */
    private boolean isWet;
    /** True if the wolf is shaking else False */
    public boolean isShaking;
    /** This time increases while wolf is shaking and emitting water particles. */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    
    public IDataTracker dataTracker;
	
	public EntityAbstractDog(World worldIn) {
		super(worldIn);
        this.setSize(0.6F, 0.85F);
	}
	
	public abstract Entity getEntityWeAreRiding();
	
	public abstract void dismountEntityWeAreRiding();
	
    //Do not put an override annotation here
	public abstract Entity getControllingPassenger();
	
    //Do not put an override annotation here
	public abstract boolean isBeingRidden();
	public abstract void removeEntityRidingUs();
	
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataTracker = new DogDataTracker(this);
	}

	@Override
	public float getSoundVolume() {
        return 0.4F;
    }
	   
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if(!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte)8);
		}
	}

	@Override
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;

        if(this.isBegging())
            this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
        else
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;

        if(this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else if((this.isWet || this.isShaking) && this.isShaking) {
            if(this.timeWolfIsShaking == 0.0F)
            	this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if(this.prevTimeWolfIsShaking >= 2.0F) {
                this.isWet = false;
                this.isShaking = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
                
                this.onFinishShaking();
            }

            if(this.timeWolfIsShaking > 0.4F) {
                float f = (float)this.getEntityBoundingBox().minY;
                int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

                for(int j = 0; j < i; ++j) {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
    public boolean isDogWet() {
        return this.isWet;
    }

    @SideOnly(Side.CLIENT)
    public float getShadingWhileWet(float partialTickTime) {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTickTime) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float partialTickTime, float p_70923_2_) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTickTime + p_70923_2_) / 1.8F;

        f = MathHelper.clamp(f, 0.0F, 1.0F);

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @SideOnly(Side.CLIENT)
    public float getInterestedAngle(float partialTickTime) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * partialTickTime) * 0.15F * (float)Math.PI;
    }
    

    @SideOnly(Side.CLIENT)
    public float getTailRotation() {
        return this.isTamed() ? (0.55F - ((this.getMaxHealth() - this.getHealth()) / (this.getMaxHealth() / 20.0F)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else
            super.handleStatusUpdate(id);
    }
    
    @Override
    public double getYOffset() {
        return this.getEntityWeAreRiding() instanceof EntityPlayer ? 0.5D : 0.0D;
    }

	public boolean isBegging() {
	    return this.dataTracker.isBegging();
	}
	    
	public void setBegging(boolean flag) {
	    this.dataTracker.setBegging(flag);
	}
	
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return !stack.isEmpty() && DoggyTalentsAPI.BREED_WHITELIST.containsItem(stack);
    }
	
	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
        if(otherAnimal == this)
            return false;
        else if(!this.isTamed())
            return false;
        else if(!(otherAnimal instanceof EntityAbstractDog))
            return false;
        else {
        	EntityAbstractDog entitydog = (EntityAbstractDog)otherAnimal;
            return !entitydog.isTamed() ? false : (entitydog.isSitting() ? false : this.isInLove() && entitydog.isInLove());
        }
    }
    
    @Override
    public boolean canRiderInteract() {
        return true;
    }
	
    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof EntityPlayer;
    }
	
	private void onFinishShaking() {
		
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		//No death message
	}
}
