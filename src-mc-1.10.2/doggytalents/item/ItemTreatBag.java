package doggytalents.item;

import doggytalents.DoggyTalents;
import doggytalents.lib.GuiNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTreatBag extends ItemDT {

	public ItemTreatBag() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}
		else {
			playerIn.openGui(DoggyTalents.INSTANCE, GuiNames.GUI_ID_FOOD_BAG, worldIn, playerIn.inventory.currentItem, 0, 0);
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	    }
	}
	
}
