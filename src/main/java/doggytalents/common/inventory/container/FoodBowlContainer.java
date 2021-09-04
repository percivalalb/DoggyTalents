package doggytalents.common.inventory.container;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyContainerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author ProPercivalalb
 */
public class FoodBowlContainer extends AbstractContainerMenu {

    private BlockEntity tileEntity;

    //Server method
    public FoodBowlContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(DoggyContainerTypes.FOOD_BOWL.get(), windowId);
        this.tileEntity = world.getBlockEntity(pos);
        IItemHandler inventory = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new RuntimeException("Item handler not present."));

        for (int i = 0; i < 1; i++) {
            for (int l = 0; l < 5; l++) {
                this.addSlot(new SlotItemHandler(inventory, l + i * 9, 44 + l * 18, 22 + i * 18));
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 103));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.tileEntity.getLevel(), this.tileEntity.getBlockPos()), player, DoggyBlocks.FOOD_BOWL.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (i < 5) {
                if (!moveItemStackTo(itemstack1, 5, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(itemstack1, 0, 5, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }
}
