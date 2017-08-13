package doggytalents.base.other;

import java.util.UUID;

import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.entity.EntityDog;
import doggytalents.entity.TalentHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public abstract class EntityDogBridge extends EntityDog {

	public EntityDogBridge(World world) {
		super(world);
	}

	@Override
	public void addAIMeleeAttack(int priority, double speedIn, boolean useLongMemory) {
		this.tasks.addTask(priority, new EntityAIAttackMelee(this, speedIn, useLongMemory));
	}
	
	@Override
	public boolean isBeingRidden() {
		return !this.getPassengers().isEmpty();
	}
	
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }
	
	@Override
	public Entity getEntityWeAreRiding() {
        return this.getRidingEntity();
    }
	
	@Override
	public void dismountEntityWeAreRiding() {
		this.dismountRidingEntity();
	}
	
	@Override
	public void removeEntityRidingUs() {
		this.removePassengers();
	}
	
	@Override
	public void setOwnerUUID(UUID uuid) {
		this.setOwnerId(uuid);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.getOwnerId();
	}
	
	@Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }
	
    @Override
    protected SoundEvent getAmbientSound() {
        return this.rand.nextInt(3) == 0 ? (this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT) : SoundEvents.ENTITY_WOLF_AMBIENT;
    }
	
	/** Hurt sound is version specific can be found in appropriate EntityDogWrapper.class File**/
	//protected SoundEvent getHurtSound(DamageSource source);

	@Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return null; //TODO DOG Loot
    }
    
	@Override
    public void updatePassenger(Entity passenger){
        super.updatePassenger(passenger);

        if(passenger instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
    }
}
