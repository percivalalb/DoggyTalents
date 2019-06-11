package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.EntityNames;
import doggytalents.lib.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModEntities {

	public static final EntityType<EntityDog> DOG = null;
	public static final EntityType<EntityDoggyBeam> DOG_BEAM = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
		 @SubscribeEvent
		 public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
			 IForgeRegistry<EntityType<?>> entityRegistry = event.getRegistry();
			 
			 DoggyTalentsMod.LOGGER.info("Registering Entities");
			 entityRegistry.register(EntityType.Builder.create(EntityDog::new, EntityClassification.CREATURE).size(0.6F, 0.85F).build(EntityNames.DOG).setRegistryName(EntityNames.DOG));
			 entityRegistry.register(EntityType.Builder.<EntityDoggyBeam>create(EntityDoggyBeam::new, EntityClassification.MISC).setCustomClientFactory(EntityDoggyBeam::new).build(EntityNames.DOGGY_BEAM).setRegistryName(EntityNames.DOGGY_BEAM));
			 DoggyTalentsMod.LOGGER.info("Finished Registering Entities");
		 }
    }
}
