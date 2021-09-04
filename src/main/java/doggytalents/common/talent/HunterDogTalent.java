package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.common.entity.DogEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class HunterDogTalent {

    public static void onLootDrop(final LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();

        // Possible to be null #265
        if (damageSource != null) {
            Entity trueSource = damageSource.getEntity();
            if (trueSource instanceof DogEntity) {
                DogEntity dog = (DogEntity) trueSource;
                int level = dog.getLevel(DoggyTalents.HUNTER_DOG);

                if (dog.getRandom().nextInt(6) < level + (level >= 5 ? 1 : 0)) {
                    event.setLootingLevel(event.getLootingLevel() + level / 2);
                }
            }
        }
    }
}
