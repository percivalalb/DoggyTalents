package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class GuardDogTalent extends TalentInstance {

    private int cooldown;

    public GuardDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public TalentInstance copy() {
        GuardDogTalent inst = new GuardDogTalent(this.getTalent(), this.level);
        inst.cooldown = this.cooldown;
        return inst;
    }

    @Override
    public void init(AbstractDog dogIn) {
        this.cooldown = dogIn.tickCount;
    }

    @Override
    public void writeToNBT(AbstractDog dogIn, CompoundTag compound) {
        super.writeToNBT(dogIn, compound);
        int timeLeft = this.cooldown - dogIn.tickCount;
        compound.putInt("guardtime", timeLeft);
    }

    @Override
    public void readFromNBT(AbstractDog dogIn, CompoundTag compound) {
        super.readFromNBT(dogIn, compound);
        this.cooldown = dogIn.tickCount + compound.getInt("guardtime");
    }

    @Override
    public InteractionResultHolder<Float> attackEntityFrom(AbstractDog dogIn, DamageSource damageSource, float damage) {
        if (dogIn.level.isClientSide) {
            return InteractionResultHolder.pass(damage);
        }

        Entity entity = damageSource.getEntity();

        if (entity != null) {
            int timeLeft =  this.cooldown - dogIn.tickCount;
            if (timeLeft <= 0) {
                int blockChance = this.level() + (this.level() >= 5 ? 1 : 0);

                if (dogIn.getRandom().nextInt(12) < blockChance) {
                    this.cooldown = dogIn.tickCount + 10;
                    dogIn.playSound(SoundEvents.ITEM_BREAK, dogIn.getSoundVolume() / 2, (dogIn.getRandom().nextFloat() - dogIn.getRandom().nextFloat()) * 0.2F + 1.0F);
                    return InteractionResultHolder.fail(0F);
                }
            }
        }

        return InteractionResultHolder.pass(damage);
    }

}
