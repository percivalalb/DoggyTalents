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
        this.cooldown = dogIn.ticksExisted;
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.writeToNBT(dogIn, compound);
        int timeLeft = this.cooldown - dogIn.ticksExisted;
        compound.putInt("guardtime", timeLeft);
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.readFromNBT(dogIn, compound);
        this.cooldown = dogIn.ticksExisted + compound.getInt("guardtime");
    }

    @Override
    public ActionResult<Float> attackEntityFrom(AbstractDogEntity dogIn, DamageSource damageSource, float damage) {
        if (dogIn.world.isRemote) {
            return ActionResult.resultPass(damage);
        }

        Entity entity = damageSource.getTrueSource();

        if (entity != null) {
            int timeLeft =  this.cooldown - dogIn.ticksExisted;
            if (timeLeft <= 0) {
                int blockChance = this.level() + (this.level() >= 5 ? 1 : 0);

                if (dogIn.getRNG().nextInt(12) < blockChance) {
                    this.cooldown = dogIn.ticksExisted + 10;
                    dogIn.playSound(SoundEvents.ENTITY_ITEM_BREAK, dogIn.getSoundVolume() / 2, (dogIn.getRNG().nextFloat() - dogIn.getRNG().nextFloat()) * 0.2F + 1.0F);
                    return ActionResult.resultFail(0F);
                }
            }
        }

        return ActionResult.resultPass(damage);
    }

}
