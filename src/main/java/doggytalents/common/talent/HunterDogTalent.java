package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class HunterDogTalent {

    public static void onLootDrop(final LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();

        // Possible to be null #265
        if (damageSource != null) {
            Entity trueSource = damageSource.getTrueSource();
            if (trueSource instanceof DogEntity) {
                DogEntity dog = (DogEntity) trueSource;
                int level = dog.getLevel(DoggyTalents.HUNTER_DOG);

                if (dog.getRNG().nextInt(6) < level + (level >= 5 ? 1 : 0)) {
                    event.setLootingLevel(event.getLootingLevel() + level / 2);
                }
            }
        }
    }
}
