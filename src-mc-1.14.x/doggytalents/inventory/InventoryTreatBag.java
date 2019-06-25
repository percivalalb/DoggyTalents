package doggytalents.inventory;

import java.util.ArrayList;
import java.util.List;

import doggytalents.ModItems;
import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.inventory.container.ContainerTreatBag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class InventoryTreatBag extends Inventory implements IInventoryChangedListener, INamedContainerProvider {

    public int slot;
    public ItemStack itemstack;
    
    public InventoryTreatBag(int slotIn, ItemStack itemstackIn) {
        super(5);
        this.slot = slotIn;
        this.itemstack = itemstackIn;
        this.addListener(this);
    }
    
    @Override
    public void openInventory(PlayerEntity player) {
        if(player.isServerWorld()) {
            this.loadInventoryFromNBT();
        }
    }

    public void loadInventoryFromNBT() {
        CompoundNBT itemNBT = this.itemstack.getChildTag("inventory");
        if(itemNBT != null) {
            ListNBT nbttaglist = itemNBT.getList("Items", Constants.NBT.TAG_COMPOUND);
        
            for(int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
                int j = nbttagcompound.getByte("Slot") & 255;
        
                if(j >= 0 && j < this.getSizeInventory())
                    this.setInventorySlotContents(j, ItemStack.read(nbttagcompound));
            }
        }
        
    }

    public List<ItemStack> getContentOverview() {
        List<ItemStack> items = new ArrayList<>();
        
        for(int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i).copy();

            if(!itemstack.isEmpty()) {
                boolean found = false;
                
                for(int j = 0; j < items.size(); j++) {
                    ItemStack stack = items.get(j);
                    if(ItemStack.areItemsEqual(stack, itemstack)) {
                        stack.grow(itemstack.getCount());
                        found = true;
                    }
                }
                
                if(!found) {
                    items.add(itemstack);
                }
            }
        }
        return items;
    }
    
    @Override
    public void closeInventory(PlayerEntity player) {
        if(player.isServerWorld()) {
            ListNBT nbttaglist = new ListNBT();
            
            for(int i = 0; i < this.getSizeInventory(); ++i) {
                ItemStack itemstack = this.getStackInSlot(i);
    
                if(!itemstack.isEmpty()) {
                    CompoundNBT nbttagcompound = new CompoundNBT();
                    nbttagcompound.putByte("Slot", (byte)i);
                    itemstack.write(nbttagcompound);
                    nbttaglist.add(nbttagcompound);
                }
            }
    
            CompoundNBT itemNBT = this.itemstack.getOrCreateChildTag("inventory");
            
            itemNBT.put("Items", nbttaglist);
        }
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IDogInteractItem || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem().isFood() && stack.getItem().getFood().isMeat()); 
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerTreatBag(windowId, inventory, this.slot, this.itemstack);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.doggytalents.treat_bag");
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        /**ListNBT nbttaglist = new ListNBT();
        
        for(int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);

            if(!itemstack.isEmpty()) {
                CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.write(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        CompoundNBT itemNBT = this.itemstack.getOrCreateChildTag("inventory");
        
        itemNBT.put("Items", nbttaglist);**/
        
    }
}
