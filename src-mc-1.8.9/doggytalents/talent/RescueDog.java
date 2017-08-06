package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class RescueDog extends ITalent {

	@Override
	public void onLivingUpdate(EntityDog dog) {
		EntityPlayer player = (EntityPlayer)dog.getOwner();
		
		int level = dog.talents.getLevel(this);
		
		if (player != null && player.getHealth() <= 6 && level != 0 && dog.getDogHunger() > this.healCost(dog)) {
            player.heal((int)(level * 1.5D));
            dog.setDogHunger(dog.getDogHunger() - this.healCost(dog));
        }
	}
	
	public int healCost(EntityDog dog) {
        byte byte0 = 100;

        if (dog.talents.getLevel(this) == 5)
            byte0 = 80;

        return byte0;
    }
		
	
	@Override
	public String getKey() {
		return "rescuedog";
	}

}
