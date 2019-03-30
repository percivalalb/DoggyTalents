package doggytalents.talent;

import java.util.Collection;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;

/**
 * @author ProPercivalalb
 */
public class HunterDog extends ITalent {

	public static void onLootDrop(EntityLivingBase living, Entity source, Collection<EntityItem> drops) {
		if(source instanceof EntityDog) {
			EntityDog dog = (EntityDog)source;
			int level = dog.TALENTS.getLevel("hunterdog");

			if(dog.getRNG().nextInt(10) < level + (level == 5 ? 1 : 0)) {
				DoggyTalentsMod.LOGGER.debug("EXTRA DROP");
				for(EntityItem entityItem : drops)
					living.entityDropItem(entityItem.getItem().copy(), 0.0F);
			}
				
		}
	}
	
	@Override
	public String getKey() {
		return "hunterdog";
	}

}
