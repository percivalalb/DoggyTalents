package doggytalents.talent;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;

/**
 * @author ProPercivalalb
 */
public class BlackPelt extends ITalent {

	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.talents.getLevel(this);
		
		int critChance = level == 5 ? 1 : 0;
        critChance += level;
        
        if (dog.getRNG().nextInt(6) < critChance) {
        	damage += (damage + 1) / 2;
        	DoggyTalentsMod.proxy.spawnCrit(dog.worldObj, entity);
        }
        return damage;
	}
	
	@Override
	public String getKey() {
		return "blackpelt";
	}

}
