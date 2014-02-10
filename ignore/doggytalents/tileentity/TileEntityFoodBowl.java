package doggytalents.tileentity;

import java.util.List;
import java.util.Random;

import doggytalents.entity.EntityDTDoggy;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityFoodBowl extends TileEntity implements IInventory {
   
	private final int iDogFoodBowlInventorySize = 5;
    private final int iDogFoodBowlStackSizeLimit = 64;
    private ItemStack bowlcontents[];

    public TileEntityFoodBowl()
    {
        bowlcontents = new ItemStack[5];
    }
    
    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        bowlcontents = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < bowlcontents.length)
            {
                bowlcontents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < bowlcontents.length; i++)
        {
            if (bowlcontents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                bowlcontents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
    	List var1 = null;
        var1 = this.worldObj.getEntitiesWithinAABB(EntityDTDoggy.class, AxisAlignedBB.getBoundingBox((float)this.xCoord, (double)(float)this.yCoord + 0.5D, (float)this.zCoord, (float)(this.xCoord + 1), (double)(float)this.yCoord + 0.5D + 0.05000000074505806D, (float)(this.zCoord + 1)).expand(5, 5, 5));

        if (var1 != null && var1.size() > 0)
        {
            for (int j1 = 0; j1 < var1.size(); j1++)
            {
                EntityDTDoggy entitydtdoggy1 = (EntityDTDoggy)var1.get(j1);

                if (entitydtdoggy1.getWolfTummy() <= 60 && this.GetFirstDogFoodStack(entitydtdoggy1) >= 0)
                {
                    this.FeedDog(entitydtdoggy1, this.GetFirstDogFoodStack(entitydtdoggy1), 1);
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 5;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return bowlcontents[i];
    }

    public ItemStack GetStackInSlot(int i)
    {
        return bowlcontents[i];
    }

    private int FindSlotToStoreItemStack(ItemStack itemstack)
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack itemstack1 = GetStackInSlot(i);

            if (itemstack1 != null && itemstack1.itemID == itemstack.itemID && itemstack1.isStackable() && itemstack1.stackSize < itemstack1.getMaxStackSize() && itemstack1.stackSize < getInventoryStackLimit() && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack.getItemDamage()))
            {
                return i;
            }
        }

        return -1;
    }

    public int GetFirstEmptyStack()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            if (GetStackInSlot(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    public int FindFirstDogFoodStack()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            if (GetStackInSlot(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    public int GetFirstDogFoodStack(EntityDTDoggy entitydtdoggy)
    {
        for (int i = 0; i < 5; i++)
        {
            if (bowlcontents[i] == null)
            {
                continue;
            }

            Item item = bowlcontents[i].getItem();

            if (item == null || !(item instanceof ItemFood))
            {
                continue;
            }

            ItemStack itemstack = GetStackInSlot(i);

            if (entitydtdoggy.foodValue(itemstack) != 0)
            {
                return i;
            }
        }

        return -1;
    }

    public int StorePartialItemStack(ItemStack itemstack)
    {
        int i = itemstack.itemID;
        int j = itemstack.stackSize;
        int k = FindSlotToStoreItemStack(itemstack);

        if (k < 0)
        {
            k = GetFirstEmptyStack();
        }

        if (k < 0)
        {
            return j;
        }

        if (GetStackInSlot(k) == null)
        {
            setInventorySlotContents(k, new ItemStack(i, 0, itemstack.getItemDamage()));
        }

        int l = j;
        ItemStack itemstack1 = GetStackInSlot(k);

        if (l > itemstack1.getMaxStackSize() - itemstack1.stackSize)
        {
            l = itemstack1.getMaxStackSize() - itemstack1.stackSize;
        }

        if (l > getInventoryStackLimit() - itemstack1.stackSize)
        {
            l = getInventoryStackLimit() - itemstack1.stackSize;
        }

        if (l == 0)
        {
            return j;
        }
        else
        {
            j -= l;
            itemstack1.stackSize += l;
            setInventorySlotContents(k, itemstack1);
            return j;
        }
    }

    public boolean AddItemStackToInventory(ItemStack itemstack)
    {
        if (!itemstack.isItemDamaged())
        {
            itemstack.stackSize = StorePartialItemStack(itemstack);

            if (itemstack.stackSize == 0)
            {
                return true;
            }
        }

        int i = GetFirstEmptyStack();

        if (i >= 0)
        {
            setInventorySlotContents(i, itemstack);
            return true;
        }
        else
        {
            return false;
        }
    }

    public ItemStack FeedDog(EntityDTDoggy entitydtdoggy, int i, int j)
    {
        if (GetStackInSlot(i) != null)
        {
            ItemStack itemstack = GetStackInSlot(i);
            entitydtdoggy.setWolfTummy(entitydtdoggy.getWolfTummy() + entitydtdoggy.foodValue(itemstack));

            if (GetStackInSlot(i).stackSize <= j)
            {
                ItemStack itemstack1 = GetStackInSlot(i);
                setInventorySlotContents(i, null);
                return itemstack1;
            }

            ItemStack itemstack2 = GetStackInSlot(i).splitStack(j);

            if (GetStackInSlot(i).stackSize == 0)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                onInventoryChanged();
            }

            return itemstack2;
        }
        else
        {
            return null;
        }
    }

    public void DropContents(World world, int i, int j, int k)
    {
        label0:

        for (int l = 0; l < 5; l++)
        {
            ItemStack itemstack = GetStackInSlot(l);

            if (itemstack == null)
            {
                continue;
            }

            float f = world.rand.nextFloat() * 0.7F + 0.15F;
            float f1 = world.rand.nextFloat() * 0.7F + 0.15F;
            float f2 = world.rand.nextFloat() * 0.7F + 0.15F;

            do
            {
                if (itemstack.stackSize <= 0)
                {
                    continue label0;
                }

                int i1 = world.rand.nextInt(21) + 10;

                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }

                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
            }
            while (true);
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (GetStackInSlot(i) != null)
        {
            if (GetStackInSlot(i).stackSize <= j)
            {
                ItemStack itemstack = GetStackInSlot(i);
                setInventorySlotContents(i, null);
                return itemstack;
            }

            ItemStack itemstack1 = GetStackInSlot(i).splitStack(j);

            if (GetStackInSlot(i).stackSize == 0)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                onInventoryChanged();
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        bowlcontents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "DogFoodBowl";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        else
        {
            return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
        }
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    private boolean ContainsFood()
    {
        for (int i = 0; i < 5; i++)
        {
            if (bowlcontents[i] == null)
            {
                continue;
            }

            Item item = bowlcontents[i].getItem();

            if (item != null && (item instanceof ItemFood))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (bowlcontents[i] != null)
        {
            ItemStack itemstack = bowlcontents[i];
            bowlcontents[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
	public boolean func_94042_c() {
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack) {
		return false;
	}
}
