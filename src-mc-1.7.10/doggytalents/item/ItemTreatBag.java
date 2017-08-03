package doggytalents.item;

import doggytalents.DoggyTalents;
import doggytalents.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTreatBag extends ItemDT {

	public ItemTreatBag() {
		super("treat_bag");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		ItemStack itemstack = playerIn.getHeldItem();

		if(worldIn.isRemote) {
			return itemstack;
		}
		else {
			playerIn.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_FOOD_BAG, worldIn, playerIn.inventory.currentItem, 0, 0);
			return itemstack;
	    }
	}
}
