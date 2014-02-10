package doggytalents;

import cpw.mods.fml.common.registry.EntityRegistry;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class ModEntitys {

	public static void init() {
		registerMob(EntityDTDoggy.class, "DTDoggy", EntityRegistry.findGlobalUniqueEntityId());
	}
	
	public static void registerMob(Class entityClass, String saveName, int id) {
	    EntityRegistry.registerGlobalEntityID(entityClass, saveName, id);
	    EntityRegistry.registerModEntity(entityClass, saveName, id, DoggyTalentsMod.instance, 120, 1, true);
	}
}
