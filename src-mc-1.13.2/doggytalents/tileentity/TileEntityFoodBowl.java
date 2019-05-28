package doggytalents.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.ModTileEntities;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.lib.GuiNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityFoodBowl extends TileEntity implements ISidedInventory, IInteractionObject, ITickable {
   
	private static final int[] SLOTS_ALL = new int[]{0,1,2,3,4};
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

    public int timeoutCounter;
    
    public TileEntityFoodBowl() {
    	super(ModTileEntities.FOOD_BOWL);
    }
    
    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.furnaceItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
        return compound;
    }

    @Override
    public void tick() {
    	
    	//Only run update code every 5 ticks (0.25s)
    	if(++this.timeoutCounter < 5) { return; }
    	
    	
    	List<EntityDog> dogList = this.world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos).grow(5, 5, 5));

    	for(EntityDog dog : dogList) {
    		//TODO make dog bowl remember who placed and only their dogs can attach to the bowl
    		dog.COORDS.setBowlPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
            	
    		int slotIndex = DogUtil.getFirstSlotWithFood(dog, this);
         	if(dog.getDogHunger() < 60 && slotIndex >= 0)
         		DogUtil.feedDog(dog, this, slotIndex);
        }
    	
    	this.timeoutCounter = 0;
    }
    
    @Override
    public int getSizeInventory() {
    	return this.furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.furnaceItemStacks) {
        	if (!itemstack.isEmpty()) {
        		return false;
        	}
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.furnaceItemStacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
           stack.setCount(this.getInventoryStackLimit());
        }

    }

	@Override
	public void clear() {
		this.furnaceItemStacks.clear();
	}

	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}

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
	public int getInventoryStackLimit() {
		return 64;
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} 
		else {
			return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
		}
	}


	@Override
	public ITextComponent getCustomName() {
		return null;
	}

	@Override
	public ITextComponent getName() {
		return new TextComponentTranslation("container.doggytalents.foodbowl");
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true; // TODO
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_ALL;
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerFoodBowl(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return GuiNames.FOOD_BOWL;
	}
}
