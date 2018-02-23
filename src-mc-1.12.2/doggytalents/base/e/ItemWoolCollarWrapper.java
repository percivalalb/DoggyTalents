package doggytalents.base.e;

import java.util.List;

import doggytalents.item.ItemWoolCollar;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.11.2 Code
 */
public class ItemWoolCollarWrapper extends ItemWoolCollar {

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour")) {
			int rgb = stack.getTagCompound().getInteger("collar_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add("Colour: " + TextFormatting.RED + "" + r + TextFormatting.GREEN + " " + g + TextFormatting.BLUE + " " + b);
		}
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(EnumDyeColor color : EnumDyeColor.values()) {
			ItemStack baseColours = new ItemStack(this);
			baseColours.setTagCompound(new NBTTagCompound());
			baseColours.getTagCompound().setInteger("collar_colour", color.getMapColor().colorValue);
			subItems.add(baseColours);
        }
    }
}
