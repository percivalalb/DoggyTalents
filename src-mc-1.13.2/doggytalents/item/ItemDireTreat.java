package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDireTreat extends Item implements IDogInteractItem {
	
	public ItemDireTreat(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, EntityPlayer playerIn) {
		int level = dogIn.LEVELS.getLevel();
		int direLevel = dogIn.LEVELS.getDireLevel();
		
		if(level >= 60 && direLevel < 30 && dogIn.getGrowingAge() >= 0) {
			if(!playerIn.isCreative())
				stackIn.shrink(1);

			if(!worldIn.isRemote) {
				dogIn.LEVELS.increaseDireLevel();
				dogIn.setHealth(dogIn.getMaxHealth());
				dogIn.setSitting(true);
				dogIn.getNavigator().clearPath();
	            dogIn.world.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TextComponentTranslation("dogtreat.levelup"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, null);
        }
		else if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote){
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TextComponentTranslation("dogtreat.tooyoung"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, null);
		}
		else if(level < 60) {
			playerIn.world.setEntityState(dogIn, (byte)6);
	        if(!playerIn.world.isRemote) {
	        	dogIn.playTameEffect(false);
	            playerIn.sendMessage(new TextComponentTranslation("dogtreat.leveltoohigh"));
	        }
	        
	        return ActionResult.newResult(EnumActionResult.FAIL, null);
		}
		else {
			worldIn.setEntityState(dogIn, (byte)6);
			if (!worldIn.isRemote) {
				playerIn.sendMessage(new TextComponentTranslation("dogtreat.ultimatelevel"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, null);
		}
	}
}
