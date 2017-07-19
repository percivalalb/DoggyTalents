package doggytalents.base.d;

import java.util.List;

import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemWoolCollar;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.12 Code
 */
public class WoolCollar extends ItemWoolCollar {

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, worldIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour")) {
			int rgb = stack.getTagCompound().getInteger("collar_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add("Colour: " + TextFormatting.RED + "" + r + TextFormatting.GREEN + " " + g + TextFormatting.BLUE + " " + b);
		}
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
			for(EnumDyeColor color : EnumDyeColor.values()) {
				ItemStack baseColours = new ItemStack(this);
				baseColours.setTagCompound(new NBTTagCompound());
				baseColours.getTagCompound().setInteger("collar_colour", color.getColorValue());
	            items.add(baseColours);
	        }
        }
    }
}
