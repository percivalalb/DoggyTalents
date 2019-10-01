package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author ProPercivalalb
 */
public class RescueDogTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        PlayerEntity player = (PlayerEntity)dog.getOwner();

        int level = dog.getTalentFeature().getLevel(this);

        //TODO add particles and check how far away dog is
        if (player != null && player.getHealth() <= 6 && level != 0 && dog.getHungerFeature().getDogHunger() > this.healCost(dog)) {
            player.heal((int)(level * 1.5D));
            dog.getHungerFeature().setDogHunger(dog.getHungerFeature().getDogHunger() - this.healCost(dog));
        }
    }

    public int healCost(IDogEntity dog) {
        byte byte0 = 100;

        if (dog.getTalentFeature().getLevel(this) == 5)
            byte0 = 80;

        return byte0;
    }
}
