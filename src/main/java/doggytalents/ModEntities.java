package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.EntityNames;
import doggytalents.lib.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModEntities {

    public static final EntityType<EntityDog> DOG = null;
    public static final EntityType<EntityDoggyBeam> DOG_BEAM = null;

     public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
         IForgeRegistry<EntityType<?>> entityRegistry = event.getRegistry();
         
         DoggyTalentsMod.LOGGER.debug("Registering Entities");
         entityRegistry.register(EntityType.Builder.create(EntityDog::new, EntityClassification.CREATURE)
                 .size(0.6F, 0.85F)
                 .setUpdateInterval(3)
                 .setTrackingRange(16)
                 .setShouldReceiveVelocityUpdates(true)
                 .build(EntityNames.DOG)
                 .setRegistryName(EntityNames.DOG));
         entityRegistry.register(EntityType.Builder.<EntityDoggyBeam>create(EntityDoggyBeam::new, EntityClassification.MISC)
                 .size(0.25F, 0.25F)
                 .setUpdateInterval(4)
                 .setTrackingRange(10)
                 .setShouldReceiveVelocityUpdates(true)
                 .setCustomClientFactory(EntityDoggyBeam::new)
                 .disableSummoning()
                 .build(EntityNames.DOGGY_BEAM)
                 .setRegistryName(EntityNames.DOGGY_BEAM));
         DoggyTalentsMod.LOGGER.debug("Finished Registering Entities");
     }
}
