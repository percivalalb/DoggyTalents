package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.api.enu.WetSource;
import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.item.Items;

public class FisherDogTalent extends Talent {

    @Override
    public void onShakingDry(DogEntity dogIn, WetSource source) {
        if (dogIn.world.isRemote) { // On client do nothing
            return;
        }

        if (source.isWaterBlock()) {
            int lvlFisherDog = dogIn.getLevel(this);

            if(dogIn.getRNG().nextInt(15) < lvlFisherDog * 2) {
                int lvlHellHound = dogIn.getLevel(DoggyTalents.HELL_HOUND);
                dogIn.entityDropItem(dogIn.getRNG().nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
            }
        }
    }
}
