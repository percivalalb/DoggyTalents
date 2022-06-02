package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.api.enu.WetSource;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.item.Items;

public class FisherDogTalent extends TalentInstance {

    public FisherDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void onShakingDry(AbstractDog dogIn, WetSource source) {
        if (dogIn.level.isClientSide) { // On client do nothing
            return;
        }

        if (source.isWaterBlock()) {
            if (dogIn.getRandom().nextInt(15) < this.level() * 2) {
                int lvlHellHound = dogIn.getDogLevel(DoggyTalents.HELL_HOUND);
                dogIn.spawnAtLocation(dogIn.getRandom().nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
            }
        }
    }
}
