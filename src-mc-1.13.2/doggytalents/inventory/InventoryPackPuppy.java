package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy extends InventoryBasic implements IInteractionObject {
	
    private EntityDog dog;

    public InventoryPackPuppy(EntityDog dog) {
    	super(new TextComponentTranslation("container.packpuppy"), 15);
        this.dog = dog;
    }

	
	public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = tagCompound.getList("packpuppyitems", 10);
        
        for(int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompound(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory())
            	  this.setInventorySlotContents(j, ItemStack.read(nbttagcompound1));
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.getSizeInventory(); ++i) {
            if(!this.getStackInSlot(i).isEmpty()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.getStackInSlot(i).write(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        tagCompound.setTag("packpuppyitems", nbttaglist);
    }
    
    @Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerPackPuppy(player, this.dog);
	}

	@Override
	public String getGuiID() {
		return "doggytalents:packpuppy";
	}
}
