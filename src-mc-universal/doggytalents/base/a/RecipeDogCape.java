package doggytalents.base.a;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.base.ObjectLib;
import doggytalents.item.ItemCapeColoured;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeDogCape implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack itemstack = ObjectLib.STACK_UTIL.getEmpty();
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!ObjectLib.STACK_UTIL.isEmpty(itemstack1))
            {
                if (itemstack1.getItem() instanceof ItemCapeColoured)
                {
                	ItemCapeColoured itemarmor = (ItemCapeColoured)itemstack1.getItem();

                    if(!ObjectLib.STACK_UTIL.isEmpty(itemstack))
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.DYE)
                    {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !ObjectLib.STACK_UTIL.isEmpty(itemstack) && !list.isEmpty();
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ObjectLib.STACK_UTIL.getEmpty();
        int[] aint = new int[3];
        int i = 0;
        int count = 0; //The number of different sources of colour
        ItemCapeColoured itemWoolCollar = null;

        for(int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if(!ObjectLib.STACK_UTIL.isEmpty(itemstack1)) {
                if(itemstack1.getItem() instanceof ItemCapeColoured) {
                    itemWoolCollar = (ItemCapeColoured)itemstack1.getItem();

                    itemstack = itemstack1.copy();
                    ObjectLib.STACK_UTIL.setCount(itemstack, 1);

                    if(itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("cape_colour")) {
                        int l = itemstack1.getTagCompound().getInteger("cape_colour");
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
                    if(itemstack1.getItem() != Items.DYE)
                        return ObjectLib.STACK_UTIL.getEmpty();

                    float[] afloat = ObjectLib.METHODS.getRGB(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));
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
            return ObjectLib.STACK_UTIL.getEmpty();
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
        return ObjectLib.STACK_UTIL.getEmpty();
    }

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }

	@Override
	public int getRecipeSize() {
        return 10;
    }
}