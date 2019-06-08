package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemTinyBone extends Item implements IDogInteractItem {

	public ItemTinyBone(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, PlayerEntity playerIn) {
		if(dogIn.getGrowingAge() >= 0) {
			if(!playerIn.isCreative())
				stackIn.shrink(1);

			if(!playerIn.world.isRemote) {
				dogIn.setDogSize(dogIn.getDogSize() - 1);
			}
			return ActionResult.newResult(ActionResultType.SUCCESS, stackIn);
		}
		else {
			if(!playerIn.world.isRemote){
				playerIn.sendMessage(new TranslationTextComponent("treat.tiny_bone.too_young"));
			}
			return ActionResult.newResult(ActionResultType.FAIL, stackIn);
		}	
	}
}