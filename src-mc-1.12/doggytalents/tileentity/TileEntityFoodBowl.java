package doggytalents.tileentity;

import java.util.List;

import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class TileEntityFoodBowl extends TileEntity implements ITickable, IInventory {
   
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> bowlContents;
    public int timeoutCounter;
    
    
    public TileEntityFoodBowl() {
    	 this.inventoryTitle = "container.foodBowl";
         this.slotsCount = 5;
    	 this.bowlContents = NonNullList.<ItemStack>withSize(this.slotsCount, ItemStack.EMPTY);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList nbttaglist = tag.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < this.slotsCount) {
            	this.setInventorySlotContents(j, new ItemStack(nbttagcompound1));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.slotsCount; i++) {
            if(!this.getStackInSlot(i).isEmpty()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.getStackInSlot(i).writeToNBT(nbttagcompound1);
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
    	
    	List<EntityDog> dogList = this.world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY() + 0.5D, this.pos.getZ(), this.pos.getX() + 1.0D, this.pos.getY() + 0.5D + 0.05000000074505806D, this.pos.getZ() + 1.0D).grow(5, 5, 5));

    	for(EntityDog dog : dogList) {
    		dog.coords.setBowlPos(this.pos);
            	
    		int slotIndex = DogUtil.getFirstSlotWithFood(dog, this);
         	if(dog.getDogHunger() < 60 && slotIndex >= 0)
         		DogUtil.feedDog(dog, this, slotIndex);
        }
    	
    	this.timeoutCounter = 0;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.bowlContents.size() ? (ItemStack)this.bowlContents.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.bowlContents, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < this.slotsCount; ++i)
        {
            ItemStack itemstack1 = this.getStackInSlot(i);

            if (itemstack1.isEmpty())
            {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.grow(k);
                    itemstack.shrink(k);

                    if (itemstack.isEmpty())
                    {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack itemstack = (ItemStack)this.bowlContents.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.bowlContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.bowlContents.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getSizeInventory()
    {
        return this.slotsCount;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.bowlContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getName()
    {
        return this.inventoryTitle;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
    	
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }
    
    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        this.bowlContents.clear();
    }

}
