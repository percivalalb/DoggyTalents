package doggytalents;

import doggytalents.entity.EntityDog;
import doggytalents.inventory.container.ContainerFoodBowl;
import doggytalents.inventory.container.ContainerPackPuppy;
import doggytalents.inventory.container.ContainerTreatBag;
import doggytalents.lib.GuiNames;
import doggytalents.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModContainerTypes {
    
    public static final ContainerType<ContainerFoodBowl> FOOD_BOWL = null;
    public static final ContainerType<ContainerPackPuppy> PACK_PUPPY = null;
    public static final ContainerType<ContainerTreatBag> TREAT_BAG = null;
    
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> containerRegistry = event.getRegistry();
        
        DoggyTalentsMod.LOGGER.debug("Registering Containers");
        containerRegistry.register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return new ContainerFoodBowl(windowId, Minecraft.getInstance().player.world, pos, inv, Minecraft.getInstance().player);
        }).setRegistryName(GuiNames.FOOD_BOWL));

        containerRegistry.register(IForgeContainerType.create((windowId, inv, data) -> {
            Entity entity = inv.player.world.getEntityByID(data.readInt());
            if(entity instanceof EntityDog) {
                return new ContainerPackPuppy(windowId, inv, (EntityDog)entity);
            } else {
                return null;
            }
        }).setRegistryName(GuiNames.PACK_PUPPY));

        containerRegistry.register(IForgeContainerType.create((windowId, inv, data) -> {
            int slotId = data.readByte();
            return new ContainerTreatBag(windowId, inv, slotId, data.readItemStack());
        }).setRegistryName(GuiNames.TREAT_BAG));
        DoggyTalentsMod.LOGGER.debug("Finished Registering Containers");
    }
}