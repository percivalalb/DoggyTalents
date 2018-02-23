package doggytalents.base.c;

import java.util.List;

import doggytalents.item.ItemCapeColoured;
import doggytalents.item.ItemWoolCollar;
import doggytalents.lib.TextFormatting;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.9.4 Code
 */
public class ItemCapeColouredWrapper extends ItemCapeColoured {

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("cape_colour")) {
			int rgb = stack.getTagCompound().getInteger("cape_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add("Colour: " + TextFormatting.RED + "" + r + TextFormatting.GREEN + " " + g + TextFormatting.BLUE + " " + b);
		}
	}
}
