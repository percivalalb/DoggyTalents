package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.inventory.container.ContainerFoodBowl;
import doggytalents.inventory.container.ContainerPackPuppy;
import doggytalents.inventory.container.ContainerTreatBag;
import doggytalents.lib.GuiNames;
import doggytalents.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModContainerTypes {
    
    public static final ContainerType<ContainerFoodBowl> FOOD_BOWL = null;
    public static final ContainerType<ContainerPackPuppy> PACK_PUPPY = null;
    public static final ContainerType<ContainerTreatBag> TREAT_BAG = null;
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
            IForgeRegistry<ContainerType<?>> containerRegistry = event.getRegistry();
            
            DoggyTalentsMod.LOGGER.debug("Registering Containers");
            containerRegistry.register(new ContainerType<ContainerFoodBowl>(ContainerFoodBowl::new).setRegistryName(GuiNames.FOOD_BOWL));
            containerRegistry.register(new ContainerType<ContainerPackPuppy>(new IContainerFactory<ContainerPackPuppy>() {

                @Override
                public ContainerPackPuppy create(int windowId, PlayerInventory inv, PacketBuffer data) {
                    Entity entity = inv.player.world.getEntityByID(data.readInt());
                    if(entity instanceof EntityDog) {
                        return new ContainerPackPuppy(windowId, inv, (EntityDog)entity);
                    } else {
                        return null;
                    }
                }
                
            }).setRegistryName(GuiNames.PACK_PUPPY));
            containerRegistry.register(IForgeContainerType.create(ContainerTreatBag::new).setRegistryName(GuiNames.TREAT_BAG));
            DoggyTalentsMod.LOGGER.debug("Finished Registering Containers");
        }
    }
}