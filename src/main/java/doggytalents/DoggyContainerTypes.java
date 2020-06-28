package doggytalents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.inventory.container.TreatBagContainer;
import doggytalents.common.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DoggyContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

    public static final RegistryObject<ContainerType<FoodBowlContainer>> FOOD_BOWL = register("food_bowl", (windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new FoodBowlContainer(windowId, inv.player.world, pos, inv, inv.player);
    });
    public static final RegistryObject<ContainerType<PackPuppyContainer>> PACK_PUPPY = register("pack_puppy", (windowId, inv, data) -> {
        Entity entity = inv.player.world.getEntityByID(data.readInt());
        return entity instanceof DogEntity ? new PackPuppyContainer(windowId, inv, (DogEntity) entity) : null;
    });
    public static final RegistryObject<ContainerType<TreatBagContainer>> TREAT_BAG = register("treat_bag", (windowId, inv, data) -> {
        int slotId = data.readByte();
        return new TreatBagContainer(windowId, inv, slotId, data.readItemStack());
    });
    public static final RegistryObject<ContainerType<DogInventoriesContainer>> DOG_INVENTORIES = register("dog_inventories", (windowId, inv, data) -> {
        int noDogs = data.readInt();
        List<DogEntity> dogs = new ArrayList<>(noDogs);
        IntArray array = new IntArray(noDogs);
        for (int i = 0; i < noDogs; i++) {
            Entity entity = inv.player.world.getEntityByID(data.readInt());
            if (entity instanceof DogEntity) {
                dogs.add((DogEntity) entity);
                array.set(i, entity.getEntityId());
            }
        }
        return !dogs.isEmpty() ? new DogInventoriesContainer(windowId, inv, array) : null;
    });

    private static <X extends Container, T extends ContainerType<X>> RegistryObject<ContainerType<X>> register(final String name, final IContainerFactory<X> factory) {
        return register(name, () -> IForgeContainerType.create(factory));
    }

    private static <T extends ContainerType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return CONTAINERS.register(name, sup);
    }
}