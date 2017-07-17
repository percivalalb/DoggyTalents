package doggytalents.item;

import java.util.List;

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

public class ItemWoolCollar extends ItemDT {

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
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
	
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("collar_colour", 3);
        
    }

    public int getColor(ItemStack stack) {
       
    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound != null)    
    		return nbttagcompound.getInteger("collar_colour");

    	return 10511680;
      
    }

    public void removeColor(ItemStack stack) {

    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound != null)
    		nbttagcompound.removeTag("collar_colour");
        
    }

    public void setColor(ItemStack stack, int color) {

    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound == null) {	
    		nbttagcompound = new NBTTagCompound();
    		stack.setTagCompound(nbttagcompound);
    	}
            
    	nbttagcompound.setInteger("collar_colour", color);
    }
}
