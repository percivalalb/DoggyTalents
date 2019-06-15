package doggytalents.api.registry;

import net.minecraft.item.ItemStack;

public class CustomIngredient {

	private final ItemStack[] matchingStacks;

	protected CustomIngredient(ItemStack... matchingStacksIn) {
		this.matchingStacks = matchingStacksIn;
	}
	
	public boolean apply(ItemStack stack) {
        if(stack == null) {
            return false;
        }
        else {
            for (ItemStack itemstack : this.matchingStacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    int i = itemstack.getMetadata();

                    if (i == 32767 || i == stack.getMetadata()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
	
	public ItemStack getStack() {
		if(this.matchingStacks.length > 0) {
			return this.matchingStacks[0];
		} else {
			return null;
		}
	}
	
	public static CustomIngredient fromStacks(ItemStack... stacks) {
        if(stacks.length > 0) {
            for(ItemStack itemstack : stacks) {
                if(!itemstack.isEmpty()) {
                    return new CustomIngredient(stacks);
                }
            }
        }

        return EMPTY;
    }
	
	public static final CustomIngredient EMPTY = new CustomIngredient(new ItemStack[0]) {
		@Override
		public boolean apply(ItemStack stack) {
			return stack.isEmpty();
		}
	};
}
