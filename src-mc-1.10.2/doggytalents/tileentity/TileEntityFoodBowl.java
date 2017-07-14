package doggytalents.tileentity;

import java.util.List;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class TileEntityFoodBowl extends TileEntity implements ITickable, IInventory {
   
	private final int iDogFoodBowlInventorySize = 5;
    private final int iDogFoodBowlStackSizeLimit = 64;
    private ItemStack[] bowlContents;
    public int timeoutCounter;

    public TileEntityFoodBowl() {
        this.bowlContents = new ItemStack[5];
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        this.bowlContents = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < this.bowlContents.length) {
            	this.bowlContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.bowlContents.length; i++) {
            if (this.bowlContents[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.bowlContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tag.setTag("Items", nbttaglist);
		return tag;
    }

    @Override
    public void update() {
    	
    	//Only run update code every 5 ticks (0.25s)
    	if(++this.timeoutCounter < 5) { return; }
    	
    	List<EntityDog> dogList = this.worldObj.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY() + 0.5D, this.pos.getZ(), this.pos.getX() + 1.0D, this.pos.getY() + 0.5D + 0.05000000074505806D, this.pos.getZ() + 1.0D).expand(5, 5, 5));

    	for(EntityDog dog : dogList) {
    		dog.coords.setBowlPos(this.pos);
            	
         	if(dog.getDogHunger() < 60 && this.getFirstDogFoodStack(dog) >= 0) {
                this.feedDog(dog, this.getFirstDogFoodStack(dog), 1);
         	}
        }
    	
    	this.timeoutCounter = 0;
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.bowlContents[i];
    }

    private int findSlotToStoreItemStack(ItemStack itemstack)
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack itemstack1 = getStackInSlot(i);

            if (itemstack1 != null && itemstack1.getItem() == itemstack.getItem() && itemstack1.isStackable() && itemstack1.stackSize < itemstack1.getMaxStackSize() && itemstack1.stackSize < getInventoryStackLimit() && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack.getItemDamage()))
            {
                return i;
            }
        }

        return -1;
    }

    public int getFirstEmptyStack()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            if (getStackInSlot(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    public int getFirstDogFoodStack(EntityDog dog) {
        for (int i = 0; i < 5; i++) {
            if (bowlContents[i] == null)
                continue;

            Item item = bowlContents[i].getItem();

            if (item == null || !(item instanceof ItemFood))
                continue;

            ItemStack itemstack = getStackInSlot(i);

            if(dog.foodValue(itemstack) != 0)
                return i;
        }

        return -1;
    }
    
    public ItemStack feedDog(EntityDog dog, int i, int j) {
        if (getStackInSlot(i) != null) {
            ItemStack itemstack = getStackInSlot(i);
            dog.setDogHunger(dog.getDogHunger() + dog.foodValue(itemstack));

            if (getStackInSlot(i).stackSize <= j) {
                ItemStack itemstack1 = getStackInSlot(i);
                setInventorySlotContents(i, null);
                return itemstack1;
            }

            ItemStack itemstack2 = getStackInSlot(i).splitStack(j);

            if(getStackInSlot(i).stackSize == 0)
                this.setInventorySlotContents(i, null);
            else
                this.markDirty();

            return itemstack2;
        }
        else
            return null;
    }

    public void dropContents(World world, int i, int j, int k) {
        label0:

        for (int l = 0; l < 5; l++)
        {
            ItemStack itemstack = getStackInSlot(l);

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
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                entityitem.setPickupDelay(10);
                world.spawnEntityInWorld(entityitem);
            }
            while (true);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (getStackInSlot(i) != null)
        {
            if (getStackInSlot(i).stackSize <= j)
            {
                ItemStack itemstack = getStackInSlot(i);
                setInventorySlotContents(i, null);
                return itemstack;
            }

            ItemStack itemstack1 = getStackInSlot(i).splitStack(j);

            if (getStackInSlot(i).stackSize == 0) {
                setInventorySlotContents(i, null);
            }
            else
            {
                this.markDirty();
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        bowlContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    	return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

    private boolean ContainsFood()
    {
        for (int i = 0; i < 5; i++)
        {
            if (bowlContents[i] == null)
            {
                continue;
            }

            Item item = bowlContents[i].getItem();

            if (item != null && (item instanceof ItemFood))
            {
                return true;
            }
        }

        return false;
    }

    @Override
	public String getName() {
		return "container.foodBowl";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int i = 0; i < this.bowlContents.length; ++i)
			this.bowlContents[i] = null;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		if(this.bowlContents[index] != null) {
            ItemStack itemstack = this.bowlContents[index];
            this.bowlContents[index] = null;
            return itemstack;
        }
        else
            return null;
	}
}
