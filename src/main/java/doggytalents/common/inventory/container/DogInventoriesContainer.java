package doggytalents.common.inventory.container;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyTalents;
import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.slot.DogInventorySlot;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author ProPercivalalb
 */
public class DogInventoriesContainer extends Container implements IContainerListener {

    private World world;
    private PlayerEntity player;
    public IntReferenceHolder position;
    private IIntArray trackableArray;
    public final List<DogInventorySlot> dogSlots = Lists.newArrayList();
    public int possibleSlots = 0;

    //Server method
    public DogInventoriesContainer(int windowId, PlayerInventory playerInventory, IIntArray trackableArray) {
        super(DoggyContainerTypes.DOG_INVENTORIES.get(), windowId);
        this.world = playerInventory.player.world;
        this.player = playerInventory.player;
        this.trackableArray = trackableArray;
        this.position = IntReferenceHolder.single();
        assertIntArraySize(trackableArray, 1);
        this.trackInt(this.position);
        this.trackIntArray(trackableArray);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addDogSlots();
    }

    public void addDogSlots() {
        final int TOTAL_COLUMNS = 9;

        int page = this.position.get();
        int drawingColumn = 0;
        DoggyTalents2.LOGGER.debug("Add slots");
        for (int i = 0; i < this.trackableArray.size(); i++) {
            int entityId = this.trackableArray.get(i);
            Entity entity = this.world.getEntityByID(entityId);
            if (entity instanceof DogEntity) {
                DogEntity dog = (DogEntity) entity;
                ItemStackHandler packInventory = entity.getCapability(PackPuppyTalent.PACK_PUPPY_CAPABILITY).orElseThrow(() -> new RuntimeException("Item handler not present."));

                int level = MathHelper.clamp(dog.getLevel(DoggyTalents.PACK_PUPPY), 0, 5); // Number of rows for this dog
                int numCols = MathHelper.clamp(level, 0, Math.max(0, TOTAL_COLUMNS)); // Number of rows to draw

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < numCols; col++) {
                        DogInventorySlot slot = new DogInventorySlot(dog, this.player, packInventory, drawingColumn + col, row, col, col * 3 + row, 8 + 18 * (drawingColumn + col - page), 18 * row + 18);
                        this.addDogSlot(slot);
                        if (slot.getOverallColumn() - page < 0 || slot.getOverallColumn() - page >= 9) {
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
                int row = slot.getRow();
                int col = slot.getColumn();
                DogInventorySlot newSlot = new DogInventorySlot(slot.getDog(), slot.getPlayer(), slot.getItemHandler(), slot.getOverallColumn(), slot.getRow(), slot.getColumn(), slot.getSlotIndex(), 8 + 18 * (slot.getOverallColumn() - data), 18 * row + 18);
                newSlot.slotNumber = slot.slotNumber;
                this.dogSlots.set(i, newSlot);
                this.inventorySlots.set(slot.slotNumber, newSlot);
                if (slot.getOverallColumn() - data < 0 || slot.getOverallColumn() - data >= 9) {
                    newSlot.setEnabled(false);
                }
            }

//            if (world.isRemote) {
//
//            DoggyTalents2.LOGGER.debug("Cleint!!!", data, this.world.isRemote);
//                removeDogSlot();
//                addDogSlots();
//                return;
//            }
//            DoggyTalents2.LOGGER.debug("Change {}, Client {}", data, this.world.isRemote);
//            removeDogSlot();
//            addDogSlots();
////            this.detectAndSendChanges();
////            for (Slot slot : this.dogSlots) {
////                ItemStack stack = slot.getStack().copy();
////                this.inventoryItemStacks.set(slot.slotNumber, stack);
////                for(IContainerListener icontainerlistener : this.listeners) {
////                    icontainerlistener.sendSlotContents(this, slot.slotNumber, stack);
////                 }
////            }
//
//            //this.detectAndSendChanges();
        }

    }

    public void addDogSlot(DogInventorySlot slotIn) {
        this.addSlot(slotIn);
        this.dogSlots.add(slotIn);
    }

    public void removeDogSlot() {
        for (Slot slot : this.dogSlots) {
            int index = slot.slotNumber;
            for (int i = index; i < this.inventorySlots.size(); i++) {
                this.inventorySlots.get(i).slotNumber--;
            }
            this.inventorySlots.remove(index);
            this.inventoryItemStacks.remove(index);
        }
        this.dogSlots.clear();
        this.possibleSlots = 0;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int startIndex = this.inventorySlots.size() - this.dogSlots.size() + this.position.get() * 3;
            int endIndex = Math.min(startIndex + 9 * 3, this.inventorySlots.size());

            if(i >= this.inventorySlots.size() - this.dogSlots.size() && i < this.inventorySlots.size()) {
                if(!mergeItemStack(itemstack1, 0, this.inventorySlots.size() - this.dogSlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(!mergeItemStack(itemstack1, this.inventorySlots.size() - this.dogSlots.size(), this.inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if(itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if(itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }

    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

    }

//    @Override
//    public void tick() {
//
//    }
}
