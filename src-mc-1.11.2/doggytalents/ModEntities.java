package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void inti() {
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "dog"), EntityDog.class, Reference.MOD_ID + ":dog", 0, DoggyTalentsMod.instance, 120, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "attackbeam"), EntityDoggyBeam.class, Reference.MOD_ID + ":attackbeam", 1, DoggyTalentsMod.instance, 64, 10, true);
	}
}
