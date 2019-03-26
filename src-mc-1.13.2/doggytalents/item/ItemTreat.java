package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemTreat extends Item implements IDogInteractItem {

	private int maxLevel;
	public ItemTreat(int maxLevel, Properties properties) {
		super(properties);
		this.maxLevel = maxLevel;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, EntityPlayer playerIn) {
		int level = dogIn.LEVELS.getLevel();
		
		if(level < this.maxLevel && dogIn.getGrowingAge() >= 0) {
			if(!playerIn.isCreative())
				stackIn.shrink(1);

			if(!playerIn.world.isRemote) {
	            dogIn.LEVELS.increaseLevel();
	            dogIn.setHealth(dogIn.getMaxHealth());
	            dogIn.setSitting(true);
	            dogIn.world.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TextComponentTranslation("dogtreat.levelup"));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, null);
        }
		else if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote) {
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TextComponentTranslation("dogtreat.tooyoung"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, null);
		}
		else {
			playerIn.world.setEntityState(dogIn, (byte)6);
			if(!playerIn.world.isRemote) {
				dogIn.playTameEffect(false);
				playerIn.sendMessage(new TextComponentTranslation("dogtreat.leveltoohigh"));
			}
			
			return ActionResult.newResult(EnumActionResult.FAIL, null);
		}
	}
}
