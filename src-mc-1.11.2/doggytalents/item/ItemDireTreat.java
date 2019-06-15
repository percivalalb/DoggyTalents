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
public class ItemDireTreat extends ItemDT implements IDogInteractItem {
	
	public ItemDireTreat() {
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, EntityPlayer playerIn) {
		int level = dogIn.LEVELS.getLevel();
		int direLevel = dogIn.LEVELS.getDireLevel();
		
		if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote){
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TextComponentTranslation("treat.dire_treat.too_young"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, stackIn);
		}
		else if(level >= 60 && direLevel < 30) {
			if(!playerIn.capabilities.isCreativeMode)
				stackIn.shrink(1);

			if(!worldIn.isRemote) {
				dogIn.LEVELS.increaseDireLevel();
				dogIn.setHealth(dogIn.getMaxHealth());
				dogIn.getAISit().setSitting(true);
				dogIn.getNavigator().clearPathEntity();
				worldIn.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TextComponentTranslation("treat.dire_treat.level_up"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, stackIn);
        }
		else if(level < 60) {
			worldIn.setEntityState(dogIn, (byte)6);
	        if(!worldIn.isRemote) {
	        	dogIn.playTameEffect(false);
	            playerIn.sendMessage(new TextComponentTranslation("treat.dire_treat.low_level"));
	        }
	        
	        return ActionResult.newResult(EnumActionResult.FAIL, stackIn);
		}
		else {
			worldIn.setEntityState(dogIn, (byte)6);
			if (!worldIn.isRemote) {
				playerIn.sendMessage(new TextComponentTranslation("treat.dire_treat.max_level"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, stackIn);
		}
	}
}
