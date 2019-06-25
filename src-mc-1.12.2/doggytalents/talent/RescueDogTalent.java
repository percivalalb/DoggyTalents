package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class RescueDogTalent extends Talent {

    @Override
    public void livingTick(EntityDog dog) {
        EntityPlayer player = (EntityPlayer)dog.getOwner();
        
        int level = dog.TALENTS.getLevel(this);
        
        //TODO add particles and check how far away dog is
        if (player != null && player.getHealth() <= 6 && level != 0 && dog.getDogHunger() > this.healCost(dog)) {
            player.heal((int)(level * 1.5D));
            dog.setDogHunger(dog.getDogHunger() - this.healCost(dog));
        }
    }
    
    public int healCost(EntityDog dog) {
        byte byte0 = 100;

        if (dog.TALENTS.getLevel(this) == 5)
            byte0 = 80;

        return byte0;
    }
}
