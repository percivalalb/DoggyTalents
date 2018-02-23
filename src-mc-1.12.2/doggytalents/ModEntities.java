package doggytalents;

import doggytalents.base.ObjectLib;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void init() {
		ObjectLib.REGISTRY.registerEntity(ObjectLib.ENTITY_DOG_CLASS, new ResourceLocation(Reference.MOD_ID, "dog"), 0, DoggyTalents.INSTANCE, 80, 3, true);
		ObjectLib.REGISTRY.registerEntity(ObjectLib.ENTITY_DOGGY_BEAM_CLASS, new ResourceLocation(Reference.MOD_ID, "attackbeam"), 1, DoggyTalents.INSTANCE, 64, 10, true);
	}
}
