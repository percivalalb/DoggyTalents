package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author ProPercivalalb
 */
public class HappyEaterTalent extends Talent {

	@Override
	public int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) {
		int level = dog.TALENTS.getLevel(this);
		if(foodValue == 0) {
	       if ((stack.getItem() == Items.COD || stack.getItem() == Items.COOKED_COD || stack.getItem() == Items.SALMON || stack.getItem() == Items.COOKED_SALMON || stack.getItem() == Items.TROPICAL_FISH) && level == 5)
	        	foodValue = 30 + 3 * level;
	
	        if (stack.getItem() == Items.ROTTEN_FLESH && level >= 3)
	        	foodValue = 30 + 3 * level;
		}
		else {
			if(stack.getItem() != Items.ROTTEN_FLESH && stack.getItem().isFood()) {
	            Food food = stack.getItem().getFood();
	            if(food.isMeat())
	            	foodValue += 4 * level;
	        }
		}
		return foodValue; 
	}
}
