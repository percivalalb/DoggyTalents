package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void init() {
		EntityRegistry.registerModEntity(EntityDog.class, "dog", 0, DoggyTalents.INSTANCE, 120, 1, true);
		EntityRegistry.registerModEntity(EntityDoggyBeam.class, "attackbeam", 1, DoggyTalents.INSTANCE, 64, 10, true);
	}
}
