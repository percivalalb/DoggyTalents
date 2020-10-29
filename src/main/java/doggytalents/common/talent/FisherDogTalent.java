package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.api.enu.WetSource;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.item.Items;

public class FisherDogTalent extends TalentInstance {

    public FisherDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void onShakingDry(AbstractDogEntity dogIn, WetSource source) {
        if (dogIn.world.isRemote) { // On client do nothing
            return;
        }

        if (source.isWaterBlock()) {
            if (dogIn.getRNG().nextInt(15) < this.level() * 2) {
                int lvlHellHound = dogIn.getLevel(DoggyTalents.HELL_HOUND);
                dogIn.entityDropItem(dogIn.getRNG().nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
            }
        }
    }
}
