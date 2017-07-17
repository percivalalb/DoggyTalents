package doggytalents.item;

import doggytalents.DoggyTalents;
import doggytalents.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCoinBag extends ItemDT {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, itemstack);
		}
		else {
			playerIn.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_FOOD_BAG, worldIn, playerIn.inventory.currentItem, 0, 0);
			return new ActionResult(EnumActionResult.PASS, itemstack);
	    }
	}
}
