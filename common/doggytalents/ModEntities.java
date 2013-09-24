package doggytalents;

import cpw.mods.fml.common.registry.EntityRegistry;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void inti() {
		registerEntity(EntityDTDoggy.class, "DTDoggy", 0);
	}
	
	public static void registerEntity(Class entityClass, String saveName, int id) {
	    EntityRegistry.registerModEntity(entityClass, saveName, id, DoggyTalentsMod.instance, 120, 2, true);
	}
}
