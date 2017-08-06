package doggytalents.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWoolCollar extends ItemDT {
	
	public ItemWoolCollar() {
		super();
	} 

	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("collar_colour", 3);
        
    }
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(stack.hasTagCompound())
			if(stack.getTagCompound().hasKey("collar_colour"))
				return stack.getTagCompound().getInteger("collar_colour");
		return -1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour")) {
			int rgb = stack.getTagCompound().getInteger("collar_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add("Colour: " + EnumChatFormatting.RED + "" + r + EnumChatFormatting.GREEN + " " + g + EnumChatFormatting.BLUE + " " + b);
		}
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for(int i = 0; i < 16; i++) {
			float[] rgb = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(i));
			
			ItemStack baseColours = new ItemStack(this);
			baseColours.setTagCompound(new NBTTagCompound());
			int i1 = (int)(rgb[0] * 255F);
            int j1 = (int)(rgb[1] * 255F);
            int k1 = (int)(rgb[2] * 255F);
			baseColours.getTagCompound().setInteger("collar_colour", (((i1 << 8) + j1) << 8) + k1);
			subItems.add(baseColours);
        }
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
