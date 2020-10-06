package doggytalents.common;

import java.util.List;

import doggytalents.DoggyItems;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.common.block.tileentity.FoodBowlTileEntity;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.inventory.container.TreatBagContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class Screens {

    public static class PackPuppyContainerProvider implements INamedContainerProvider {

        private AbstractDogEntity dog;

        public PackPuppyContainerProvider(AbstractDogEntity dogIn) {
            this.dog = dogIn;
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
            return new PackPuppyContainer(windowId, inventory, this.dog);
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.doggytalents.pack_puppy");
        }
    }

    public static class DogInventoriesContainerProvider implements INamedContainerProvider {

        private List<DogEntity> dogs;

        public DogInventoriesContainerProvider(List<DogEntity> dogIn) {
            this.dogs = dogIn;
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
            IntArray array = new IntArray(this.dogs.size());
            for (int i = 0; i < array.size(); i++) {
                array.set(i, this.dogs.get(i).getEntityId());
            }
            return new DogInventoriesContainer(windowId, inventory, array);
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.doggytalents.dog_inventories");
        }
    }

    public static class TreatBagContainerProvider implements INamedContainerProvider {

        private ItemStack stack;
        private int slotId;

        public TreatBagContainerProvider(ItemStack stackIn, int slotId) {
            this.stack = stackIn;
            this.slotId = slotId;
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
            return new TreatBagContainer(windowId, inventory, this.slotId, this.stack);
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.doggytalents.treat_bag");
        }
    }

    public static void openPackPuppyScreen(ServerPlayerEntity player, AbstractDogEntity dogIn) {
        if (dogIn.isAlive()) {
            NetworkHooks.openGui(player, new PackPuppyContainerProvider(dogIn), (buf) -> {
                buf.writeInt(dogIn.getEntityId());
            });
        }
    }

    public static void openDogInventoriesScreen(ServerPlayerEntity player, List<DogEntity> dogIn) {
        if (!dogIn.isEmpty()) {
            NetworkHooks.openGui(player, new DogInventoriesContainerProvider(dogIn), (buf) -> {
                buf.writeInt(dogIn.size());
                for (DogEntity dog : dogIn) {
                    buf.writeInt(dog.getEntityId());
                }
            });
        }
    }

    public static void openFoodBowlScreen(ServerPlayerEntity player, FoodBowlTileEntity foodBowl) {
        NetworkHooks.openGui(player, foodBowl, foodBowl.getPos());
    }

    public static void openTreatBagScreen(ServerPlayerEntity player, ItemStack stackIn, int slotId) {
        if (stackIn.getItem() == DoggyItems.TREAT_BAG.get()) {
            NetworkHooks.openGui(player, new TreatBagContainerProvider(stackIn, slotId), buf -> {
                buf.writeVarInt(slotId);
                buf.writeItemStack(stackIn);
            });
        }
    }

}
