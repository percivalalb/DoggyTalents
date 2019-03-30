package doggytalents.entity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityAbstractDog extends EntityTameable {
	
	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityAbstractDog.class, DataSerializers.FLOAT);
	/** Float used to smooth the rotation of the wolf head */
	private float headRotationCourse;
	private float headRotationCourseOld;
	/** true is the wolf is wet else false */
	public boolean isWet;
	/** True if the wolf is shaking else False */
	private boolean isShaking;
	/** This time increases while wolf is shaking and emitting water particles. */
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityAbstractDog(EntityType<?> type, World worldIn) {
		super(type, worldIn);
		this.setSize(0.6F, 0.85F);
		this.setTamed(false);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
		if(this.isTamed()) {
			this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		} 
		else {
			this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		}

		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}

	@Override
	protected void updateAITasks() {
		this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(DATA_HEALTH_ID, this.getHealth());
	}

	@Override
	protected void playStepSound(BlockPos pos, IBlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		if(this.rand.nextInt(3) == 0) {
			return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
		} 
		else {
      	  return SoundEvents.ENTITY_WOLF_AMBIENT;
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
	}

	@Override
	public float getSoundVolume() {
		return 0.4F;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WOLF;
	}

	@Override
	public void livingTick() {
		super.livingTick();
		if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte)8);
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.headRotationCourseOld = this.headRotationCourse;
		if (this.isBegging()) {
			this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
		} 
		else {
			this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
		}

		if (this.isInWaterRainOrBubbleColumn()) {
			this.isWet = true;
			this.isShaking = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if ((this.isWet || this.isShaking) && this.isShaking) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWet = false;
				this.isShaking = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
				
				this.onFinishShaking();
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float f = (float)this.getBoundingBox().minY;
				int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for(int j = 0; j < i; ++j) {
					float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.world.addParticle(Particles.SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
	}

   /**
    * True if the wolf is wet
    */
	@OnlyIn(Dist.CLIENT)
   	public boolean isDogWet() {
		return this.isWet;
	}

   /**
    * Used when calculating the amount of shading to apply while the wolf is wet.
    */
   	@OnlyIn(Dist.CLIENT)
   	public float getShadingWhileWet(float p_70915_1_) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
   	}

   	@OnlyIn(Dist.CLIENT)
   	public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
	   float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
	   if (f < 0.0F) {
		   f = 0.0F;
	   } else if (f > 1.0F) {
		   f = 1.0F;
	   }

	   return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
   	}

   	@OnlyIn(Dist.CLIENT)
   	public float getInterestedAngle(float p_70917_1_) {
	   return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
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
   	public boolean attackEntityAsMob(Entity entityIn) {
   		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
   		if (flag) {
   			this.applyEnchantments(this, entityIn);
   		}

   		return flag;
   	}

   	@Override
   	public void setTamed(boolean tamed) {
   		super.setTamed(tamed);
   		if (tamed) {
   			this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
   		} else {
   			this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
   		}

   		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   	}
   	
   	@Override
   	@OnlyIn(Dist.CLIENT)
   	public void handleStatusUpdate(byte id) {
   		if (id == 8) {
   			this.isShaking = true;
   			this.timeWolfIsShaking = 0.0F;
   			this.prevTimeWolfIsShaking = 0.0F;
   		} else {
   			super.handleStatusUpdate(id);
   		}

   	}

   	@OnlyIn(Dist.CLIENT)
   	public float getTailRotation() {
   		return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
   	}
   	
   	@Override
   	public boolean canMateWith(EntityAnimal otherAnimal) {
   		if (otherAnimal == this) {
   			return false;
   		} else if (!this.isTamed()) {
   			return false;
   		} else if (!(otherAnimal instanceof EntityAbstractDog)) {
   			return false;
   		} else {
   			EntityAbstractDog entitywolf = (EntityAbstractDog)otherAnimal;
   			if (!entitywolf.isTamed()) {
   				return false;
   			} else if (entitywolf.isSitting()) {
   				return false;
   			} else {
   				return this.isInLove() && entitywolf.isInLove();
   			}
   		}
   	}

   	@Override
   	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
   		if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
   			if (target instanceof EntityAbstractDog) {
   				EntityAbstractDog entitywolf = (EntityAbstractDog)target;
   				if (entitywolf.isTamed() && entitywolf.getOwner() == owner) {
   					return false;
   				}
   			}

   			if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target)) {
   				return false;
   			} else {
   				return !(target instanceof AbstractHorse) || !((AbstractHorse)target).isTame();
   			}
   		} else {
   			return false;
   		}
   	}
   	
	protected abstract void onFinishShaking();
	protected abstract boolean isBegging();

   	class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {
   		private final EntityAbstractDog wolf;

   		public AIAvoidEntity(EntityAbstractDog wolfIn, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_) {
   			super(wolfIn, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
   			this.wolf = wolfIn;
   		}

   		@Override
   		public boolean shouldExecute() {
   			if (super.shouldExecute() && this.closestLivingEntity instanceof EntityLlama) {
   				return !this.wolf.isTamed() && this.avoidLlama((EntityLlama)this.closestLivingEntity);
   			} else {
   				return false;
   			}
   		}

   		private boolean avoidLlama(EntityLlama p_190854_1_) {
   			return p_190854_1_.getStrength() >= EntityAbstractDog.this.rand.nextInt(5);
   		}

   		@Override
   		public void startExecuting() {
   			EntityAbstractDog.this.setAttackTarget((EntityLivingBase)null);
   			super.startExecuting();
   		}

   		@Override
   		public void tick() {
   			EntityAbstractDog.this.setAttackTarget((EntityLivingBase)null);
   			super.tick();
   		}
   	}
}