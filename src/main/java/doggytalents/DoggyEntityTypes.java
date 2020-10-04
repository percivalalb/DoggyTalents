package doggytalents;

import java.util.function.Function;
import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.DoggyBeamEntity;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DoggyEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<DogEntity>> DOG = register("dog", DogEntity::new, EntityClassification.CREATURE, (b) -> b
            .size(0.6F, 0.85F)
            .setUpdateInterval(3)
            .setTrackingRange(16)
            .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<DoggyBeamEntity>> DOG_BEAM = register("dog_beam", DoggyBeamEntity::new, EntityClassification.MISC, (b) -> b
            .size(0.25F, 0.25F)
            .setUpdateInterval(4)
            .setTrackingRange(10)
            .setShouldReceiveVelocityUpdates(true)
            .setCustomClientFactory(DoggyBeamEntity::new)
            .disableSummoning());

     private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final EntityType.IFactory<E> sup, final EntityClassification classification, final Function<EntityType.Builder<E>, EntityType.Builder<E>> builder) {
         return register(name, () -> builder.apply(EntityType.Builder.create(sup, classification)).build(Util.getResourcePath(name)));
     }

     private static <E extends Entity, T extends EntityType<E>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
         return ENTITIES.register(name, sup);
     }

     public static void addEntityAttributes() {
         GlobalEntityTypeAttributes.put(DOG.get(),
                 MobEntity.func_233666_p_()
                 .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                 .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D)
                 .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                 .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                 .createMutableAttribute(DoggyAttributes.JUMP_POWER.get(), 0.42D)
                 .createMutableAttribute(DoggyAttributes.CRIT_CHANCE.get(), 0.01D)
                 .createMutableAttribute(DoggyAttributes.CRIT_BONUS.get(), 1D)
                 .create()
         );
     }
}
