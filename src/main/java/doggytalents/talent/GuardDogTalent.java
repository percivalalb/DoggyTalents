package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class GuardDogTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("guardtime", 0);
    }

    @Override
    public void livingTick(IDogEntity dog) {
        int guardTime = dog.getObject("guardtime", Integer.TYPE);
        if(guardTime > 0) {
            dog.putObject("guardtime", guardTime - 1);
        }
    }

    @Override
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) {
        Entity entity = damageSource.getTrueSource();
        int guardTime = dog.getObject("guardtime", Integer.TYPE);

        if (entity != null && guardTime <= 0) {
            int level = dog.getTalentFeature().getLevel(this);
            int blockChance = level != 5 ? 0 : 1;
            blockChance += level;

            if (dog.getRNG().nextInt(12) < blockChance) {
                dog.putObject("guardtime", 10);
                dog.playSound(SoundEvents.ENTITY_ITEM_BREAK, dog.getSoundVolume(), (dog.getRNG().nextFloat() - dog.getRNG().nextFloat()) * 0.2F + 1.0F);

                return false;
            }
        }

        return true;
    }

    @Override
    public int onRegenerationTick(IDogEntity dog, int totalInTick) {
        if(dog.getTalentFeature().getLevel(this) >= 5)
            if(dog.getRNG().nextInt(2) == 0)
                totalInTick += 1;
        return totalInTick;
    }
}
