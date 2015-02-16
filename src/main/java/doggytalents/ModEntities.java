package doggytalents;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.Reference;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void inti() {
		registerEntity(EntityDog.class, "dog");
		registerEntity(EntityDoggyBeam.class, "attackbeam");
		EntityRegistry.registerModEntity(EntityDoggyBeam.class, Reference.MOD_ID + ":attackbeam", 0, DoggyTalentsMod.instance, 64, 10, true);
	}
	
	public static void registerEntity(Class entityClass, String saveName) {
	    EntityRegistry.registerGlobalEntityID(entityClass, Reference.MOD_ID + ":" + saveName, EntityRegistry.findGlobalUniqueEntityId());
	}
}
