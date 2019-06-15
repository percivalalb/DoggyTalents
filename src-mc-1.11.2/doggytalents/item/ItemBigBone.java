package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemBigBone extends ItemDT implements IDogInteractItem {
	
	public ItemBigBone() {
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, EntityPlayer playerIn) {
		if(dogIn.getGrowingAge() >= 0) {
			if(!playerIn.isCreative())
				stackIn.shrink(1);

			if(!playerIn.world.isRemote) {
				dogIn.setDogSize(dogIn.getDogSize() + 1);
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, stackIn);
		}
		else {
			if(!playerIn.world.isRemote){
				playerIn.sendMessage(new TextComponentTranslation("treat.big_bone.too_young"));
			}
			return ActionResult.newResult(EnumActionResult.FAIL, stackIn);
		}	
	}
}