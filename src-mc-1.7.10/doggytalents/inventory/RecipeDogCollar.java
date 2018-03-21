package doggytalents.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.item.ItemWoolCollar;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeDogCollar implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack itemstack = null;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (itemstack1 != null)
            {
                if (itemstack1.getItem() instanceof ItemWoolCollar)
                {
                	ItemWoolCollar itemarmor = (ItemWoolCollar)itemstack1.getItem();

                    if(itemstack != null)
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.dye)
                    {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return itemstack != null && !list.isEmpty();
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = null;
        int[] aint = new int[3];
        int i = 0;
        int count = 0; //The number of different sources of colour
        ItemWoolCollar itemWoolCollar = null;

        for(int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if(itemstack1 != null) {
                if(itemstack1.getItem() instanceof ItemWoolCollar) {
                    itemWoolCollar = (ItemWoolCollar)itemstack1.getItem();

                    itemstack = itemstack1.copy();
                    itemstack.stackSize = 1;

                    if(itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("collar_colour")) {
                        int l = itemstack1.getTagCompound().getInteger("collar_colour");
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
                    if(itemstack1.getItem() != Items.dye)
                        return null;

                    float[] afloat = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(itemstack1.getItemDamage())];
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
            return null;
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

	@Override
    public ItemStack getRecipeOutput() {
        return null;
	}

	@Override
	public int getRecipeSize() {
        return 10;
    }
}