package doggytalents.inventory.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.ModRecipes;
import doggytalents.item.ItemWoolCollar;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * 1.12 Code
 */
public class RecipeDogCollar extends SpecialRecipe {

	public RecipeDogCollar(ResourceLocation resource) {
		super(resource);
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		ItemStack itemstack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if(!itemstack1.isEmpty()) {
                if(itemstack1.getItem() instanceof ItemWoolCollar) {
                	if(!itemstack.isEmpty()) {
                		return false;
                	}

                    itemstack = itemstack1;
                }
                else {
                	if(DyeColor.getColor(itemstack1) == null) {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !itemstack.isEmpty() && !list.isEmpty();
    }

	@Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack itemstack = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int count = 0; //The number of different sources of colour
        ItemWoolCollar itemWoolCollar = null;

        for(int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if(!itemstack1.isEmpty()) {
            	Item item = itemstack1.getItem();
                if(item instanceof ItemWoolCollar) {
                    itemWoolCollar = (ItemWoolCollar)item;
                    if(!itemstack.isEmpty()) {
                    	return ItemStack.EMPTY;
                    }
                    
                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);
                    if(itemWoolCollar.hasColor(itemstack1)) {
                        int l = itemstack1.getTag().getInt("collar_colour");
                        float f = (float)(l >> 16 & 255) / 255.0F;
                        float f1 = (float)(l >> 8 & 255) / 255.0F;
                        float f2 = (float)(l & 255) / 255.0F;
                        i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int)((float)aint[0] + f * 255.0F);
                        aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                        aint[2] = (int)((float)aint[2] + f2 * 255.0F);
                        ++count;
                    }
                }
                else {
                	DyeColor color = DyeColor.getColor(itemstack1);
                    if (color == null) {
                       return ItemStack.EMPTY;
                    }
                    
                    float[] afloat = color.getColorComponentValues();
                    int l1 = (int)(afloat[0] * 255.0F);
                    int i2 = (int)(afloat[1] * 255.0F);
                    int j2 = (int)(afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    aint[0] += l1;
                    aint[1] += i2;
                    aint[2] += j2;
                    ++count;
                }
            }
        }

        if(itemWoolCollar == null)
            return ItemStack.EMPTY;
        else {
            int i1 = aint[0] / count;
            int j1 = aint[1] / count;
            int k1 = aint[2] / count;
            float f3 = (float)i / (float)count;
            float f4 = (float)Math.max(i1, Math.max(j1, k1));
            i1 = (int)((float)i1 * f3 / f4);
            j1 = (int)((float)j1 * f3 / f4);
            k1 = (int)((float)k1 * f3 / f4);
            int k2 = (i1 << 8) + j1;
            k2 = (k2 << 8) + k1;
            itemWoolCollar.setColor(itemstack, k2);
            return itemstack;
        }
    }
	
	//Is on a 3x3 grid or bigger
	@Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.COLLAR_COLOURING;
	}
}