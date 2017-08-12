package doggytalents.base.c;

import doggytalents.item.ItemRadar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRadarWrapper extends ItemRadar {

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return this.onItemRightClickGENERAL(worldIn, playerIn, handIn);
	}
}
