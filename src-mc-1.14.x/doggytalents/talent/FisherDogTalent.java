package doggytalents.talent;

import doggytalents.ModTalents;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.item.Items;

/**
 * @author ProPercivalalb
 */
public class FisherDogTalent extends Talent {
    
    @Override
    public boolean canBreatheUnderwater(EntityDog dog) { 
        return dog.TALENTS.getLevel(this) == 5;
    }
    
    @Override
    public void onFinishShaking(EntityDog dogIn, boolean gotWetInWater) {
        if(!dogIn.world.isRemote) {
            int lvlFisherDog = dogIn.TALENTS.getLevel(this);
            int lvlHellHound = dogIn.TALENTS.getLevel(ModTalents.HELL_HOUND);

            if(gotWetInWater && dogIn.getRNG().nextInt(15) < lvlFisherDog * 2)
                dogIn.entityDropItem(dogIn.getRNG().nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
        }
    }
}
