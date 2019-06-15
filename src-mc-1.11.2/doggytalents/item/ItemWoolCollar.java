package doggytalents.item;

import java.util.List;

import doggytalents.helper.DogUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWoolCollar extends ItemDT {
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		int[] rgb = ItemCapeColoured.WHITE;
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour")) {
			rgb = DogUtil.rgbIntToIntArray(stack.getTagCompound().getInteger("collar_colour"));
		}

		tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".tooltip", TextFormatting.RED + "" + rgb[0] + TextFormatting.GREEN + " " + rgb[1] + TextFormatting.BLUE + " " + rgb[2]).getFormattedText());
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
			for(EnumDyeColor color : EnumDyeColor.values()) {
				ItemStack baseColours = new ItemStack(this);
				baseColours.setTagCompound(new NBTTagCompound());
				float[] colourComponents = color.getColorComponentValues();
				int colour = (int) (colourComponents[0] * 255F);
				colour = (int) ((colour << 8) + colourComponents[1] * 255F);
				colour = (int) ((colour << 8) + colourComponents[2] * 255F);
				
				
				baseColours.getTagCompound().setInteger("collar_colour", colour);
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
