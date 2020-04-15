package doggytalents;

import doggytalents.api.lib.Reference;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author ProPercivalalb
 */
public class ModEntities {
    
    public static void init() {
        registerEntity(EntityDog.class, new ResourceLocation(Reference.MOD_ID, "dog"), 0, DoggyTalents.INSTANCE, 32, 3, true);
        registerEntity(EntityDoggyBeam.class, new ResourceLocation(Reference.MOD_ID, "attackbeam"), 1, DoggyTalents.INSTANCE, 10, 4, true);
    }
    
    public static void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(entityName, entityClass, entityName.toString().replace(":", "."), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
}
