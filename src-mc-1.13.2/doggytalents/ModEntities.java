package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.EntityNames;
import doggytalents.lib.Reference;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModEntities {

	@ObjectHolder(EntityNames.DOG)
	public static EntityType<EntityDog> DOG;
	@ObjectHolder(EntityNames.DOGGY_BEAM)
	public static EntityType<EntityDoggyBeam> DOGGY_BEAM;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
		 @SubscribeEvent
		 public static void registerBlocks(final RegistryEvent.Register<EntityType<?>> event) {
			 IForgeRegistry<EntityType<?>> entityRegistry = event.getRegistry();
			 
			 entityRegistry.register(EntityType.Builder.create(EntityDog.class, EntityDog::new).build(EntityNames.DOG).setRegistryName(EntityNames.DOG));
			 entityRegistry.register(EntityType.Builder.create(EntityDoggyBeam.class, EntityDoggyBeam::new).build(EntityNames.DOGGY_BEAM).setRegistryName(EntityNames.DOGGY_BEAM));
		 }
    }
}
