package doggytalents.talent;

import doggytalents.ModTalents;
import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.item.Items;

/**
 * @author ProPercivalalb
 */
public class FisherDogTalent extends Talent {

    @Override
    public boolean canBreatheUnderwater(IDogEntity dog) {
        return dog.getTalentFeature().getLevel(this) == 5;
    }

    @Override
    public void onFinishShaking(IDogEntity dogIn, boolean gotWetInWater) {
        if(!dogIn.world.isRemote) {
            int lvlFisherDog = dogIn.getTalentFeature().getLevel(this);
            int lvlHellHound = dogIn.getTalentFeature().getLevel(ModTalents.HELL_HOUND);

            if(gotWetInWater && dogIn.getRNG().nextInt(15) < lvlFisherDog * 2)
                dogIn.entityDropItem(dogIn.getRNG().nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
        }
    }
}
