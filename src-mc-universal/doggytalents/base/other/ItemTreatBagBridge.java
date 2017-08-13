package doggytalents.base.other;

import doggytalents.DoggyTalents;
import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.item.ItemTreatBag;
import doggytalents.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ItemTreatBagBridge extends ItemTreatBag {

	public ActionResult<ItemStack> onItemRightClickGENERAL(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
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
