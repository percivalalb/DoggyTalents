package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemTreat extends ItemDT implements IDogInteractItem {

	private int maxLevel;
	public ItemTreat(int maxLevel) {
		super();
		this.maxLevel = maxLevel;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, EntityPlayer playerIn) {
		int level = dogIn.LEVELS.getLevel();
		
		if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote) {
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TextComponentTranslation("treat.normal_treat.too_young"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, stackIn);
		}
		if(level < this.maxLevel) {
			if(!playerIn.capabilities.isCreativeMode)
				stackIn.shrink(1);

			if(!playerIn.world.isRemote) {
	            dogIn.LEVELS.increaseLevel();
	            dogIn.setHealth(dogIn.getMaxHealth());
	            dogIn.getAISit().setSitting(true);
	            worldIn.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TextComponentTranslation("treat.normal_treat.level_up"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, stackIn);
        }
		else {
			worldIn.setEntityState(dogIn, (byte)6);
			if(!worldIn.isRemote) {
				dogIn.playTameEffect(false);
				playerIn.sendMessage(new TextComponentTranslation("treat.normal_treat.max_level"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, stackIn);
		}
	}
}
