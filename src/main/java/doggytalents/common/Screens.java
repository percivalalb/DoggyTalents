package doggytalents.common;

import java.util.List;

import doggytalents.DoggyItems;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.common.block.tileentity.FoodBowlTileEntity;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.inventory.container.TreatBagContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class Screens {

    public static class PackPuppyContainerProvider implements MenuProvider {

        private AbstractDogEntity dog;

        public PackPuppyContainerProvider(AbstractDogEntity dogIn) {
            this.dog = dogIn;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
            return new PackPuppyContainer(windowId, inventory, this.dog);
        }

        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("container.doggytalents.pack_puppy");
        }
    }

    public static class DogInventoriesContainerProvider implements MenuProvider {

        private List<DogEntity> dogs;

        public DogInventoriesContainerProvider(List<DogEntity> dogIn) {
            this.dogs = dogIn;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
            SimpleContainerData array = new SimpleContainerData(this.dogs.size());
            for (int i = 0; i < array.getCount(); i++) {
                array.set(i, this.dogs.get(i).getId());
            }
            return new DogInventoriesContainer(windowId, inventory, array);
        }

        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("container.doggytalents.dog_inventories");
        }
    }

    public static class TreatBagContainerProvider implements MenuProvider {

        private ItemStack stack;
        private int slotId;

        public TreatBagContainerProvider(ItemStack stackIn, int slotId) {
            this.stack = stackIn;
            this.slotId = slotId;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
            return new TreatBagContainer(windowId, inventory, this.slotId, this.stack);
        }

        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("container.doggytalents.treat_bag");
        }
    }

    public static void openPackPuppyScreen(ServerPlayer player, AbstractDogEntity dogIn) {
        if (dogIn.isAlive()) {
            NetworkHooks.openGui(player, new PackPuppyContainerProvider(dogIn), (buf) -> {
                buf.writeInt(dogIn.getId());
            });
        }
    }

    public static void openDogInventoriesScreen(ServerPlayer player, List<DogEntity> dogIn) {
        if (!dogIn.isEmpty()) {
            NetworkHooks.openGui(player, new DogInventoriesContainerProvider(dogIn), (buf) -> {
                buf.writeInt(dogIn.size());
                for (DogEntity dog : dogIn) {
                    buf.writeInt(dog.getId());
                }
            });
        }
    }

    public static void openFoodBowlScreen(ServerPlayer player, FoodBowlTileEntity foodBowl) {
        NetworkHooks.openGui(player, foodBowl, foodBowl.getBlockPos());
    }

    public static void openTreatBagScreen(ServerPlayer player, ItemStack stackIn, int slotId) {
        if (stackIn.getItem() == DoggyItems.TREAT_BAG.get()) {
            NetworkHooks.openGui(player, new TreatBagContainerProvider(stackIn, slotId), buf -> {
                buf.writeVarInt(slotId);
                buf.writeItem(stackIn);
            });
        }
    }

}
