package doggytalents.common.inventory.container.slot;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DogInventorySlot extends SlotItemHandler {

    private boolean enabled = true;
    private PlayerEntity player;
    private DogEntity dog;
    private int overallColumn, row, col;

    public DogInventorySlot(DogEntity dogIn, PlayerEntity playerIn, IItemHandler itemHandler, int overallColumn, int row, int col, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = playerIn;
        this.overallColumn = overallColumn;
        this.row = row;
        this.col = col;
        this.dog = dogIn;
    }

    public DogInventorySlot(DogInventorySlot prev, int newX) {
        super(prev.getItemHandler(), prev.getSlotIndex(), newX, prev.y);
        this.player = prev.player;
        this.overallColumn = prev.overallColumn;
        this.row = prev.row;
        this.col = prev.col;
        this.dog = prev.dog;
        // Work around to set Slot#slotNumber (MCP name) which is Slot#index in official
        // mappings. Needed because SlotItemHandler#index shadows the latter.
        {
            Slot n = this;
            Slot o = prev;
            n.index = o.index;
        }
    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    // Don't accept items when disabled, this means disabled slots cannot be shift clicked into
    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.isActive() && super.mayPlace(stack);
    }

//    @Override
//    public boolean canTakeStack(PlayerEntity playerIn) {
//        return super.canTakeStack(playerIn);
//    }

    @Override
    public boolean isActive() {
        return this.enabled && this.dog.isAlive() && this.dog.distanceToSqr(this.player) < 400;
    }

    public DogEntity getDog() {
        return this.dog;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public int getOverallColumn() {
        return this.overallColumn;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.col;
    }
}
