package doggytalents.api.registry;

import net.minecraft.item.ItemStack;

public class CustomIngredient {

	private final ItemStack[] matchingStacks;


	protected CustomIngredient(ItemStack... matchingStacksIn) {
		this.matchingStacks = matchingStacksIn;
	}
	
	public boolean apply(ItemStack p_apply_1_) {
        if (p_apply_1_ == null)
        {
            return false;
        }
        else
        {
            for (ItemStack itemstack : this.matchingStacks)
            {
                if (itemstack.getItem() == p_apply_1_.getItem())
                {
                    int i = itemstack.getMetadata();

                    if (i == 32767 || i == p_apply_1_.getMetadata())
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
	
	public static CustomIngredient fromStacks(ItemStack... stacks)
    {
        if (stacks.length > 0)
        {
            for (ItemStack itemstack : stacks)
            {
                if (!itemstack.isEmpty())
                {
                    return new CustomIngredient(stacks);
                }
            }
        }

        return EMPTY;
    }
	
	public static final CustomIngredient EMPTY = new CustomIngredient(new ItemStack[0])
	{
		public boolean apply(ItemStack p_apply_1_)
		{
			return p_apply_1_.isEmpty();
		}
	};
}
