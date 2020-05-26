package doggytalents;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import doggytalents.inventory.container.ContainerFoodBowl;
import doggytalents.inventory.container.ContainerPackPuppy;
import doggytalents.inventory.container.ContainerTreatBag;
import doggytalents.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerFoodBowl>> FOOD_BOWL = register("food_bowl", (windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerFoodBowl(windowId, inv.player.world, pos, inv, inv.player);
    });
    public static final RegistryObject<ContainerType<ContainerPackPuppy>> PACK_PUPPY = register("pack_puppy", (windowId, inv, data) -> {
        Entity entity = inv.player.world.getEntityByID(data.readInt());
        return entity instanceof EntityDog ? new ContainerPackPuppy(windowId, inv, (EntityDog) entity) : null;
    });
    public static final RegistryObject<ContainerType<ContainerTreatBag>> TREAT_BAG = register("treat_bag", (windowId, inv, data) -> {
        int slotId = data.readByte();
        return new ContainerTreatBag(windowId, inv, slotId, data.readItemStack());
    });

    private static <X extends Container, T extends ContainerType<X>> RegistryObject<ContainerType<X>> register(final String name, final IContainerFactory<X> factory) {
        return register(name, () -> IForgeContainerType.create(factory));
    }

    private static <T extends ContainerType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return CONTAINERS.register(name, sup);
    }
}