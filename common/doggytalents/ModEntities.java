package doggytalents;

import cpw.mods.fml.common.registry.EntityRegistry;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.EntityDoggyBeam;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void inti() {
		registerEntity(EntityDTDoggy.class, "DTDoggy", 0);
		registerEntity(EntityDoggyBeam.class, "DTBeam", 1);
	}
	
	public static void registerEntity(Class entityClass, String saveName, int id) {
	    EntityRegistry.registerModEntity(entityClass, saveName, id, DoggyTalentsMod.instance, 120, 1, true);
	}
}
