package doggytalents.inventory;

import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * @author ProPercivalalb
 */
public class ContainerPackPuppy extends Container {
	
    private EntityDog dog;

    public ContainerPackPuppy(int windowId, PlayerEntity player, EntityDog dog) {
    	super(null, windowId); //TODO
        this.dog = dog;
        IInventory inventory = (IInventory)dog.objects.get("packpuppyinventory");
        inventory.openInventory(player);
        int packpuppyLevel = MathHelper.clamp(this.dog.TALENTS.getLevel(ModTalents.PACK_PUPPY), 0, 5);

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < packpuppyLevel; i1++)
                this.addSlot(new SlotPackPuppy(inventory, i1 * 3 + j, 79 + 18 * i1, 1 + 18 * j + 24, dog));
        }
        
        int var3;
        int var4;
        
        for (var3 = 0; var3 < 3; ++var3)
            for (var4 = 0; var4 < 9; ++var4)
                this.addSlot(new Slot(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));

        for (var3 = 0; var3 < 9; ++var3)
            this.addSlot(new Slot(player.inventory, var3, 8 + var3 * 18, 142));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(i);
        int packpuppyLevel = MathHelper.clamp(this.dog.TALENTS.getLevel(ModTalents.PACK_PUPPY), 0, 5);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i < 3 * packpuppyLevel) {
                if(!this.mergeItemStack(itemstack1, 3 * packpuppyLevel, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if(!this.mergeItemStack(itemstack1, 0, 3 * packpuppyLevel, false))
                return ItemStack.EMPTY;

            if(itemstack1.isEmpty())
            	slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
            
            if(itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return ((IInventory)this.dog.objects.get("packpuppyinventory")).isUsableByPlayer(player);
    }
    
    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        IInventory inventory = (IInventory)dog.objects.get("packpuppyinventory");
        inventory.closeInventory(player);
    }
}
