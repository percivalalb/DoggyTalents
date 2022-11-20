package doggytalents;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.inventory.container.TreatBagContainer;
import doggytalents.common.lib.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DoggyContainerTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.Keys.MENU_TYPES, Constants.MOD_ID);

    public static final RegistryObject<MenuType<FoodBowlContainer>> FOOD_BOWL = register("food_bowl", (windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new FoodBowlContainer(windowId, inv.player.level, pos, inv, inv.player);
    });
    public static final RegistryObject<MenuType<PackPuppyContainer>> PACK_PUPPY = register("pack_puppy", (windowId, inv, data) -> {
        Entity entity = inv.player.level.getEntity(data.readInt());
        return entity instanceof DogEntity ? new PackPuppyContainer(windowId, inv, (DogEntity) entity) : null;
    });
    public static final RegistryObject<MenuType<TreatBagContainer>> TREAT_BAG = register("treat_bag", (windowId, inv, data) -> {
        int slotId = data.readByte();
        return new TreatBagContainer(windowId, inv, slotId, data.readItem());
    });
    public static final RegistryObject<MenuType<DogInventoriesContainer>> DOG_INVENTORIES = register("dog_inventories", (windowId, inv, data) -> {
        int noDogs = data.readInt();
        List<DogEntity> dogs = new ArrayList<>(noDogs);
        SimpleContainerData array = new SimpleContainerData(noDogs);
        for (int i = 0; i < noDogs; i++) {
            Entity entity = inv.player.level.getEntity(data.readInt());
            if (entity instanceof DogEntity) {
                dogs.add((DogEntity) entity);
                array.set(i, entity.getId());
            }
        }
        return !dogs.isEmpty() ? new DogInventoriesContainer(windowId, inv, array) : null;
    });

    private static <X extends AbstractContainerMenu, T extends MenuType<X>> RegistryObject<MenuType<X>> register(final String name, final IContainerFactory<X> factory) {
        return register(name, () -> IForgeMenuType.create(factory));
    }

    private static <T extends MenuType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return CONTAINERS.register(name, sup);
    }
}
