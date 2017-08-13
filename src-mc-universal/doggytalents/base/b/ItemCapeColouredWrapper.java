package doggytalents.base.b;

import java.util.List;

import doggytalents.item.ItemCapeColoured;
import doggytalents.lib.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(stack.hasTagCompound())
			if(stack.getTagCompound().hasKey("cape_colour"))
				return stack.getTagCompound().getInteger("cape_colour");
		return -1;
	}
}
