package doggytalents.common.inventory.container;

import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyTalents;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author ProPercivalalb
 */
public class PackPuppyContainer extends AbstractContainerMenu {

    private AbstractDog dog;
    private ItemStackHandler packInventory;
    private int level;

    public PackPuppyContainer(int windowId, Inventory playerInventory, AbstractDog dogIn) {
        super(DoggyContainerTypes.PACK_PUPPY.get(), windowId);
        this.dog = dogIn;
        this.level = Mth.clamp(dogIn.getDogLevel(DoggyTalents.PACK_PUPPY), 0, 5);
        this.packInventory = dogIn.getCapability(PackPuppyTalent.PACK_PUPPY_CAPABILITY).orElseThrow(() -> new RuntimeException("Item handler not present."));

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < this.level; i1++) {
                this.addSlot(new SlotItemHandler(this.packInventory, i1 * 3 + j, 79 + 18 * i1, 1 + 18 * j + 24));
            }
        }

        int var3;
        int var4;

        for (var3 = 0; var3 < 3; ++var3) {
            for (var4 = 0; var4 < 9; ++var4) {
                this.addSlot(new Slot(playerInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3) {
            this.addSlot(new Slot(playerInventory, var3, 8 + var3 * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        int packpuppyLevel = Mth.clamp(this.level, 0, 5);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (i < 3 * packpuppyLevel) {
                if (!this.moveItemStackTo(itemstack1, 3 * packpuppyLevel, this.slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(itemstack1, 0, 3 * packpuppyLevel, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.dog.distanceToSqr(player) < 144D;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
    }

    public AbstractDog getDog() {
        return this.dog;
    }

    public int getDogLevel() {
        return this.level;
    }
}
