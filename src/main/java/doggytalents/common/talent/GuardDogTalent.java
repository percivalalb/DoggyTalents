package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;

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
    public void init(AbstractDogEntity dogIn) {
        this.cooldown = dogIn.tickCount;
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.writeToNBT(dogIn, compound);
        int timeLeft = this.cooldown - dogIn.tickCount;
        compound.putInt("guardtime", timeLeft);
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.readFromNBT(dogIn, compound);
        this.cooldown = dogIn.tickCount + compound.getInt("guardtime");
    }

    @Override
    public ActionResult<Float> attackEntityFrom(AbstractDogEntity dogIn, DamageSource damageSource, float damage) {
        if (dogIn.level.isClientSide) {
            return ActionResult.pass(damage);
        }

        Entity entity = damageSource.getEntity();

        if (entity != null) {
            int timeLeft =  this.cooldown - dogIn.tickCount;
            if (timeLeft <= 0) {
                int blockChance = this.level() + (this.level() >= 5 ? 1 : 0);

                if (dogIn.getRandom().nextInt(12) < blockChance) {
                    this.cooldown = dogIn.tickCount + 10;
                    dogIn.playSound(SoundEvents.ITEM_BREAK, dogIn.getSoundVolume() / 2, (dogIn.getRandom().nextFloat() - dogIn.getRandom().nextFloat()) * 0.2F + 1.0F);
                    return ActionResult.fail(0F);
                }
            }
        }

        return ActionResult.pass(damage);
    }

}
