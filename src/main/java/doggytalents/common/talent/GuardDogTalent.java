package doggytalents.common.talent;

import doggytalents.DoggyTalents2;
import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;

public class GuardDogTalent extends Talent {

    private static DataKey<Integer> COOLDOWN = DataKey.make();

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setDataIfEmpty(COOLDOWN, dogIn.ticksExisted);
    }

    @Override
    public void write(AbstractDogEntity dogIn, CompoundNBT compound) {
        int timeLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;
        compound.putInt("guardtime", timeLeft);
    }

    @Override
    public void read(AbstractDogEntity dogIn, CompoundNBT compound) {
        dogIn.setData(COOLDOWN, dogIn.ticksExisted + compound.getInt("guardtime"));
    }

    @Override
    public ActionResult<Float> attackEntityFrom(AbstractDogEntity dogIn, DamageSource damageSource, float damage) {
        if (dogIn.world.isRemote) {
            return ActionResult.resultPass(damage);
        }

        Entity entity = damageSource.getTrueSource();

        if (entity != null) {
            int timeLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;
            if (timeLeft <= 0) {
                int level = dogIn.getLevel(this);
                int blockChance = level + (level >= 5 ? 1 : 0);

                if (dogIn.getRNG().nextInt(12) < blockChance) {
                    dogIn.setData(COOLDOWN, dogIn.ticksExisted + 10);
                    dogIn.playSound(SoundEvents.ENTITY_ITEM_BREAK, dogIn.getSoundVolume() / 2, (dogIn.getRNG().nextFloat() - dogIn.getRNG().nextFloat()) * 0.2F + 1.0F);
                    DoggyTalents2.LOGGER.debug("Block attack");
                    return ActionResult.resultFail(0F);
                }
            }
        }

        return ActionResult.resultPass(damage);
    }

}
