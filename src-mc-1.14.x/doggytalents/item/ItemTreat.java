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
public class ItemTreat extends Item implements IDogInteractItem {

	private int maxLevel;
	public ItemTreat(int maxLevel, Properties properties) {
		super(properties);
		this.maxLevel = maxLevel;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, EntityDog dogIn, World worldIn, PlayerEntity playerIn) {
		int level = dogIn.LEVELS.getLevel();
		
		if (dogIn.getGrowingAge() < 0) {
			if(!worldIn.isRemote) {
				 dogIn.playTameEffect(false);
				 playerIn.sendMessage(new TranslationTextComponent("treat.normal_treat.too_young"));
			}
			
			return ActionResult.newResult(ActionResultType.FAIL, stackIn);
		}
		if(level < this.maxLevel) {
			if(!playerIn.abilities.isCreativeMode)
				stackIn.shrink(1);

			if(!playerIn.world.isRemote) {
	            dogIn.LEVELS.increaseLevel();
	            dogIn.setHealth(dogIn.getMaxHealth());
	            dogIn.getAISit().setSitting(true);
	            worldIn.setEntityState(dogIn, (byte)7);
	            dogIn.playTameEffect(true);
	            playerIn.sendMessage(new TranslationTextComponent("treat.normal_treat.level_up"));
			}
			
			return ActionResult.newResult(ActionResultType.SUCCESS, stackIn);
        }
		else {
			worldIn.setEntityState(dogIn, (byte)6);
			if(!worldIn.isRemote) {
				dogIn.playTameEffect(false);
				playerIn.sendMessage(new TranslationTextComponent("treat.normal_treat.max_level"));
			}
			
			return ActionResult.newResult(ActionResultType.FAIL, stackIn);
		}
	}
}
