package doggytalents;

import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void init() {
		ObjectLib.METHODS.registerEntity(ObjectLib.ENTITY_DOG_CLASS, new ResourceLocation(Reference.MOD_ID, "dog"), 0, DoggyTalents.INSTANCE, 120, 1, true);
		ObjectLib.METHODS.registerEntity(EntityDoggyBeam.class, new ResourceLocation(Reference.MOD_ID, "attackbeam"), 1, DoggyTalents.INSTANCE, 64, 10, true);
	}
}
