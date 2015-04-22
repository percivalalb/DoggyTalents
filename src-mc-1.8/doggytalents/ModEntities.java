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
		EntityRegistry.registerModEntity(EntityDog.class, Reference.MOD_ID + ":dog", 0, DoggyTalentsMod.instance, 120, 1, true);
		EntityRegistry.registerModEntity(EntityDoggyBeam.class, Reference.MOD_ID + ":attackbeam", 1, DoggyTalentsMod.instance, 64, 10, true);
	}
}
