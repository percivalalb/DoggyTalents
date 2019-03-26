package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.lib.BlockNames;
import doggytalents.lib.EntityNames;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
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
    public static class Registration
    {
		 @SubscribeEvent
		 public static void registerBlocks(final RegistryEvent.Register<EntityType<?>> event) {
			 IForgeRegistry<EntityType<?>> entityRegistry = event.getRegistry();
			 
			 entityRegistry.register(register(EntityNames.DOG, EntityType.Builder.create(EntityDog.class, EntityDog::new)).setRegistryName(EntityNames.DOG));
			 entityRegistry.register(register(EntityNames.DOG, EntityType.Builder.create(EntityDoggyBeam.class, EntityDoggyBeam::new)).setRegistryName(EntityNames.DOGGY_BEAM));
		 }
  
		 public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
			 EntityType<T> entitytype = builder.build(id);
			 return entitytype;
		}
    }
}
