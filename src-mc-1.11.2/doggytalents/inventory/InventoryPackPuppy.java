package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy extends InventoryBasic {
	
    private EntityDog dog;

    public InventoryPackPuppy(EntityDog dog) {
    	super("container.packpuppy", false, 15);
        this.dog = dog;
    }

	
	public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = tagCompound.getTagList("packpuppyitems", 10);
        
        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory())
            	  this.setInventorySlotContents(j, new ItemStack(nbttagcompound1));
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.getSizeInventory(); ++i) {
            if(!this.getStackInSlot(i).isEmpty()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tagCompound.setTag("packpuppyitems", nbttaglist);
    }
}
