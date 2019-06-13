package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import doggytalents.inventory.container.ContainerPackPuppy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy extends Inventory implements INamedContainerProvider {
	
    private EntityDog dog;

    public InventoryPackPuppy(EntityDog dog) {
    	super(15);
        this.dog = dog;
    }

	
	public void readFromNBT(CompoundNBT tagCompound) {
        ListNBT nbttaglist = tagCompound.getList("packpuppyitems", 10);
        
        for(int i = 0; i < nbttaglist.size(); ++i) {
            CompoundNBT nbttagcompound1 = (CompoundNBT)nbttaglist.getCompound(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory())
            	  this.setInventorySlotContents(j, ItemStack.read(nbttagcompound1));
        }
    }

    public void writeToNBT(CompoundNBT tagCompound) {
        ListNBT nbttaglist = new ListNBT();

        for(int i = 0; i < this.getSizeInventory(); ++i) {
            if(!this.getStackInSlot(i).isEmpty()) {
                CompoundNBT nbttagcompound1 = new CompoundNBT();
                nbttagcompound1.putByte("Slot", (byte)i);
                this.getStackInSlot(i).write(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        tagCompound.put("packpuppyitems", nbttaglist);
    }
    
    @Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new ContainerPackPuppy(windowId, inventory, (IInventory)this.dog.objects.get("packpuppyinventory"), this.dog);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.doggytalents.pack_puppy");
	}
}
