package doggytalents;

import java.util.function.Function;
import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<EntityDog>> DOG = register("dog", EntityDog::new, EntityClassification.CREATURE, (b) -> b
            .size(0.6F, 0.85F)
            .setUpdateInterval(3)
            .setTrackingRange(16)
            .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<EntityDoggyBeam>> DOG_BEAM = register("dog_beam", EntityDoggyBeam::new, EntityClassification.MISC, (b) -> b
            .size(0.25F, 0.25F)
            .setUpdateInterval(4)
            .setTrackingRange(10)
            .setShouldReceiveVelocityUpdates(true)
            .setCustomClientFactory(EntityDoggyBeam::new)
            .disableSummoning());

     private static <X extends Entity, T extends EntityType<X>> RegistryObject<EntityType<X>> register(final String name, final EntityType.IFactory<X> sup, EntityClassification classification, final Function<EntityType.Builder<X>, EntityType.Builder<X>> builder) {
         return register(name, () -> builder.apply(EntityType.Builder.create(sup, classification)).build(Reference.MOD_ID + ":" + name));
     }

     private static <T extends EntityType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
         return ENTITIES.register(name, sup);
     }
}
