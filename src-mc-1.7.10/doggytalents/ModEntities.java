package doggytalents;

import cpw.mods.fml.common.registry.EntityRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.Reference;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
	
	public static void init() {
		EntityRegistry.registerModEntity(EntityDog.class, "dog", 0, DoggyTalents.INSTANCE, 80, 3, true);
		EntityRegistry.registerModEntity(EntityDoggyBeam.class, "attackbeam", 1, DoggyTalents.INSTANCE, 64, 10, true);
		EntityList.stringToClassMapping.put("doggytalents.doggytalents:dog", EntityDog.class);
	}
}
