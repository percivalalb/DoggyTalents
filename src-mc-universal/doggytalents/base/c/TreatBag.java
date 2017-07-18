package doggytalents.base.c;

import doggytalents.item.ItemRadar;
import doggytalents.item.ItemTreatBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class TreatBag extends ItemTreatBag {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return this.onItemRightClickGENERAL(worldIn, playerIn, handIn);
	}
}
