package doggytalents.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.ModTileEntities;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.container.ContainerFoodBowl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityFoodBowl extends TileEntity implements ISidedInventory, INamedContainerProvider, ITickableTileEntity {
   
	private static final int[] SLOTS_ALL = new int[] {0,1,2,3,4};
	private NonNullList<ItemStack> bowlItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

    public int timeoutCounter;
    
    public TileEntityFoodBowl() {
    	super(ModTileEntities.FOOD_BOWL);
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.bowlItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.bowlItemStacks);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.bowlItemStacks);
        return compound;
    }

    @Override
    public void tick() {
    	
    	//Only run update code every 5 ticks (0.25s)
    	if(++this.timeoutCounter < 5) { return; }
    	
    	List<EntityDog> dogList = this.world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos).grow(5, 5, 5));

    	for(EntityDog dog : dogList) {
    		//TODO make dog bowl remember who placed and only their dogs can attach to the bowl
    		if(!dog.COORDS.hasBowlPos())
    			dog.COORDS.setBowlPos(this.pos);
            	
    		int slotIndex = DogUtil.getFirstSlotWithFood(dog, this);
         	if(dog.getDogHunger() < 60 && slotIndex >= 0)
         		DogUtil.feedDog(dog, this, slotIndex);
        }
    	
    	this.timeoutCounter = 0;
    }
    
    @Override
    public int getSizeInventory() {
    	return this.bowlItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.bowlItemStacks) {
        	if (!itemstack.isEmpty()) {
        		return false;
        	}
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.bowlItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.bowlItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.bowlItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.bowlItemStacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
           stack.setCount(this.getInventoryStackLimit());
        }

    }

	@Override
	public void clear() {
		this.bowlItemStacks.clear();
	}

	@Override
	public void openInventory(PlayerEntity player) {}
	@Override
	public void closeInventory(PlayerEntity player) {}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}


	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} 
		else {
			return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.doggytalents.food_bowl");
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true; // TODO
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if(side == Direction.DOWN) {
			return SLOTS_ALL;
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (direction == Direction.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn) {
		return new ContainerFoodBowl(windowId, playerInventory, this);
	}
}
