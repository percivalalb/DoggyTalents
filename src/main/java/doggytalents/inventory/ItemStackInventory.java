package doggytalents.inventory;

import javax.annotation.Nonnull;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemStackInventory implements IItemHandler, IItemHandlerModifiable {

    private final ItemStack itemStack;
    private int size;

    public ItemStackInventory(ItemStack itemStack, int size) {
        this.itemStack = itemStack;
        this.size = size;
    }

    private CompoundNBT getTag() {
        if (!this.itemStack.hasTag()) {
            this.itemStack.setTag(new CompoundNBT());
        }
        return this.itemStack.getTag();
    }
    
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        DoggyTalentsMod.LOGGER.info("" + stack);
        CompoundNBT itemTag = null;
        
        boolean hasStack = !stack.isEmpty();
        if (hasStack) {
            itemTag = new CompoundNBT();
            itemTag.putInt("Slot", slot);
            stack.write(itemTag);
        }
        boolean foundSlot = false;
        ListNBT tagList = this.getTag().getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT existing = tagList.getCompound(i);
            if (existing.getInt("Slot") != slot) {
                continue;
            }
            foundSlot = true;
            if (hasStack) {
                tagList.set(i, itemTag);
            } else {
                tagList.remove(i);
            }
        }
        DoggyTalentsMod.LOGGER.info("{}", tagList.toString());
        if (hasStack && !foundSlot) {
            tagList.add(itemTag);
        }
            
        this.getTag().put("Items", tagList);
        
        onContentsChanged(slot);
    }
    
    @Override
    public int getSlots() {
        return this.size;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        ListNBT tagList = this.getTag().getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT itemTags = tagList.getCompound(i);
            if (itemTags.getInt("Slot") != slot) { continue; }

            return ItemStack.read(itemTags);
        }

        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        DoggyTalentsMod.LOGGER.info("DA: " + stack);
        if (stack.isEmpty())
            return ItemStack.EMPTY;
            
        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.getStackInSlot(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
                DoggyTalentsMod.LOGGER.info("" + existing);
                existing.grow(reachedLimit ? limit : stack.getCount());
                DoggyTalentsMod.LOGGER.info("" + existing);
                this.setStackInSlot(slot, existing);
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.getStackInSlot(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.setStackInSlot(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
            }
            return existing;
        }
        else
        {
            if (!simulate)
            {
                this.setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }
    
    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack)
    {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof IDogItem || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem().isFood() && stack.getItem().getFood().isMeat());
    }
    
    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.size)
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.size + ")");
    }
    
    protected void onContentsChanged(int slot) {
        
    }
}