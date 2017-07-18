package doggytalents.base;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IStackUtil {

	public boolean isEmpty(ItemStack stack);
	
	public int getCount(ItemStack stack);
	public void shrink(ItemStack stack, int quantity);
	public void grow(ItemStack stack, int quantity);
	
	public ItemStack getEmpty();
	
	public ItemStack readFromNBT(NBTTagCompound tagCompound);
}
