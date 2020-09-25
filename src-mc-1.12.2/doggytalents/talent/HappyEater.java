package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class HappyEater extends ITalent {

	@Override
	public int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) {
		int level = dog.talents.getLevel(this);
		if(foodValue == 0) {
			//VANILLA
			if (stack.getItem() == Items.BREAD && level > 0)
	        	foodValue = 10 + 3 * level;	 
			if ((stack.getItem() == Items.APPLE || stack.getItem() == Items.BAKED_POTATO || stack.getItem() == Items.MELON) && level >= 3)
	        	foodValue = 10 + 3 * level;
	        if (stack.getItem() == Items.ROTTEN_FLESH && level == 5)
	        	foodValue = 20 + 3 * level;	  
	        //FOODMOD	        
	        if (stack.getItem() == Item.getByNameOrId("food:tomato") && level >= 4)
	        	foodValue = 10 + 3 * level;
	        if (stack.getItem() == Item.getByNameOrId("food:blue_berry") && level >= 4)
	        	foodValue = 5 + 3 * level;
	        if (stack.getItem() == Item.getByNameOrId("food:red_berry") && level >= 4)
	        	foodValue = 5 + 3 * level;
	        if (stack.getItem() == Item.getByNameOrId("food:black_berry") && level >= 4)
	        	foodValue = 5 + 3 * level;
	        if (stack.getItem() == Item.getByNameOrId("food:poisonous_berry") && level == 5)
	        	foodValue = 5 + 3 * level;
		}
		else {
			if(stack.getItem() != Items.ROTTEN_FLESH && stack.getItem() instanceof ItemFood) {
	            ItemFood itemfood = (ItemFood)stack.getItem();
	
	            if (itemfood.isWolfsFavoriteMeat())
	            	foodValue += 4 * level;
	        }
		}
		return foodValue; 
	}
	
	@Override
	public String getKey() {
		return "happyeater";
	}

}
