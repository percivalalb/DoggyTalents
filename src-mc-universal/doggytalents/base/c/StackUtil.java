package doggytalents.base.c;

import doggytalents.base.IStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 1.9.4 Code
 */
public class StackUtil implements IStackUtil {

	@Override
	public boolean isEmpty(ItemStack stack) {
		return stack == null || stack.stackSize == 0;
	}
	
	@Override
	public int getCount(ItemStack stack) {
		return stack.stackSize;
	}
	
	@Override
	public void setCount(ItemStack stack, int quantity) {
		stack.stackSize = quantity;
	}
	
	@Override
	public void shrink(ItemStack stack, int quantity) {
		stack.stackSize -= quantity;
	}

	@Override
	public void grow(ItemStack stack, int quantity) {
		stack.stackSize += quantity;
	}

	@Override
	public ItemStack getEmpty() {
		return (ItemStack)null;
	}
	
	@Override
	public ItemStack readFromNBT(NBTTagCompound tagCompound) {
		return ItemStack.func_77949_a(tagCompound);
	}
}
