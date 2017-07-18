package doggytalents.base.a;

import doggytalents.item.ItemDoggyCharm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoggyCharm extends ItemDoggyCharm {

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand handIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return this.onItemUseGENERAL(playerIn, worldIn, pos, handIn, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return this.onItemRightClickGENERAL(worldIn, playerIn, handIn);
	}
}
