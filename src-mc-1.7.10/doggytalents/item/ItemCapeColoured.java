package doggytalents.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class ItemCapeColoured extends ItemDT {
	
	public ItemCapeColoured() {
		super("cape_coloured");
	} 

	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("cape_colour", 3);
        
    }
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(stack.hasTagCompound())
			if(stack.getTagCompound().hasKey("cape_colour"))
				return stack.getTagCompound().getInteger("cape_colour");
		return -1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("cape_colour")) {
			int rgb = stack.getTagCompound().getInteger("cape_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add("Colour: " + EnumChatFormatting.RED + "" + r + EnumChatFormatting.GREEN + " " + g + EnumChatFormatting.BLUE + " " + b);
		}
	}

    public int getColor(ItemStack stack) {
       
    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound != null)    
    		return nbttagcompound.getInteger("cape_colour");

    	return 10511680;
      
    }

    public void removeColor(ItemStack stack) {

    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound != null)
    		nbttagcompound.removeTag("cape_colour");
        
    }

    public void setColor(ItemStack stack, int color) {

    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if(nbttagcompound == null) {	
    		nbttagcompound = new NBTTagCompound();
    		stack.setTagCompound(nbttagcompound);
    	}
            
    	nbttagcompound.setInteger("cape_colour", color);
    }
}
