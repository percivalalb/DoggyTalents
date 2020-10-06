package doggytalents.common.inventory.container;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyTalents;
import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.PackPuppyItemHandler;
import doggytalents.common.inventory.container.slot.DogInventorySlot;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class DogInventoriesContainer extends Container {

    private World world;
    private PlayerEntity player;
    private IntReferenceHolder position;
    private IntArray trackableArray;
    private final List<DogInventorySlot> dogSlots = Lists.newArrayList();
    private int possibleSlots = 0;

    //Server method
    public DogInventoriesContainer(int windowId, PlayerInventory playerInventory, IntArray trackableArray) {
        super(DoggyContainerTypes.DOG_INVENTORIES.get(), windowId);
        this.world = playerInventory.player.world;
        this.player = playerInventory.player;
        this.position = IntReferenceHolder.single();
        assertIntArraySize(trackableArray, 1);
        this.trackInt(this.position);
        this.trackableArray = trackableArray;

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addDogSlots();
    }

    public void addDogSlots() {
        final int TOTAL_COLUMNS = 9;

        int page = this.position.get();
        int drawingColumn = 0;

        for (int i = 0; i < this.trackableArray.size(); i++) {
            int entityId = this.trackableArray.get(i);
            Entity entity = this.world.getEntityByID(entityId);

            if (entity instanceof DogEntity) {
                DogEntity dog = (DogEntity) entity;

                PackPuppyItemHandler packInventory = dog.getData(PackPuppyTalent.PACK_PUPPY_HANDLER);
                if (packInventory == null) {
                    continue;
                }

                int level = MathHelper.clamp(dog.getLevel(DoggyTalents.PACK_PUPPY), 0, 5); // Number of rows for this dog
                int numCols = MathHelper.clamp(level, 0, Math.max(0, TOTAL_COLUMNS)); // Number of rows to draw

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < numCols; col++) {
                        DoggyTalents2.LOGGER.info("{} {}" , row, col);
                        DogInventorySlot slot = new DogInventorySlot(dog, this.player, packInventory, drawingColumn + col, row, col, col * 3 + row, 8 + 18 * (drawingColumn + col - page), 18 * row + 18);
                        this.addDogSlot(slot);
                        int adjustedColumn = slot.getOverallColumn() - page;
                        if (adjustedColumn - page < 0 || adjustedColumn - page >= 9) {
                            slot.setEnabled(false);
                        }
                    }
                }

                this.possibleSlots += level;
                drawingColumn += numCols;
            }
        }

    }

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);

        if (id == 0) {
            for (int i = 0; i < this.dogSlots.size(); i++) {
                DogInventorySlot slot = this.dogSlots.get(i);
                DogInventorySlot newSlot = new DogInventorySlot(slot, 8 + 18 * (slot.getOverallColumn() - data));
                this.replaceDogSlot(i, newSlot);
                int adjustedColumn = slot.getOverallColumn() - data;
                if (adjustedColumn < 0 || adjustedColumn >= 9) {
                    newSlot.setEnabled(false);
                }
            }
        }

    }

    private void addDogSlot(DogInventorySlot slotIn) {
        this.addSlot(slotIn);
        this.dogSlots.add(slotIn);
    }

    private void replaceDogSlot(int i, DogInventorySlot slotIn) {
        this.dogSlots.set(i, slotIn);
        this.inventorySlots.set(slotIn.slotNumber, slotIn);
    }

    public int getTotalNumColumns() {
        return this.possibleSlots;
    }

    public int getPage() {
        return this.position.get();
    }

    public void setPage(int page) {
        this.position.set(page);
    }

    public List<DogInventorySlot> getSlots() {
        return this.dogSlots;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int startIndex = this.inventorySlots.size() - this.dogSlots.size() + this.position.get() * 3;
            int endIndex = Math.min(startIndex + 9 * 3, this.inventorySlots.size());

            if (i >= this.inventorySlots.size() - this.dogSlots.size() && i < this.inventorySlots.size()) {
                if (!mergeItemStack(itemstack1, 0, this.inventorySlots.size() - this.dogSlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(itemstack1, this.inventorySlots.size() - this.dogSlots.size(), this.inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }
}
