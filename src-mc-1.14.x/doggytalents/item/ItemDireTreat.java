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

/**
 * @author ProPercivalalb
 **/
public class ItemDireTreat extends Item implements IDogInteractItem {
	
	public ItemDireTreat(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, PlayerEntity playerIn) {
		int level = dogIn.LEVELS.getLevel();
		int direLevel = dogIn.LEVELS.getDireLevel();
		
		if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote){
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TranslationTextComponent("treat.dire_treat.too_young"));
			}
			
			return ActionResult.newResult(ActionResultType.FAIL, stackIn);
		}
		else if(level >= 60 && direLevel < 30) {
			if(!playerIn.playerAbilities.isCreativeMode)
				stackIn.shrink(1);

			if(!worldIn.isRemote) {
				dogIn.LEVELS.increaseDireLevel();
				dogIn.setHealth(dogIn.getMaxHealth());
				dogIn.getAISit().setSitting(true);
				dogIn.getNavigator().clearPath();
				worldIn.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TranslationTextComponent("treat.dire_treat.level_up"));
			}
			
			return ActionResult.newResult(ActionResultType.SUCCESS, stackIn);
        }
		else if(level < 60) {
			worldIn.setEntityState(dogIn, (byte)6);
	        if(!worldIn.isRemote) {
	        	dogIn.playTameEffect(false);
	            playerIn.sendMessage(new TranslationTextComponent("treat.dire_treat.low_level"));
	        }
	        
	        return ActionResult.newResult(ActionResultType.FAIL, stackIn);
		}
		else {
			worldIn.setEntityState(dogIn, (byte)6);
			if (!worldIn.isRemote) {
				playerIn.sendMessage(new TranslationTextComponent("treat.dire_treat.max_level"));
			}
			
			return ActionResult.newResult(ActionResultType.SUCCESS, stackIn);
		}
	}
}
