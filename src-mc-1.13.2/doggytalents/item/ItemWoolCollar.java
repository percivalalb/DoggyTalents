package doggytalents.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemWoolCollar extends Item {
	
	public ItemWoolCollar(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag() && stack.getTag().contains("collar_colour")) {
			int rgb = stack.getTag().getInt("collar_colour");
			int r = (rgb >> 16) &0xFF;
			int g = (rgb >> 8) &0xFF;
			int b = (rgb >> 0) &0xFF;
		  
			tooltip.add(new TextComponentString("Colour: " + TextFormatting.RED + "" + r + TextFormatting.GREEN + " " + g + TextFormatting.BLUE + " " + b));
		}
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(this.isInGroup(group)) {
			for(EnumDyeColor color : EnumDyeColor.values()) {
				ItemStack baseColours = new ItemStack(this);
				baseColours.setTag(new NBTTagCompound());
				float[] colourComponents = color.getColorComponentValues();
				int colour = (int) (colourComponents[0] * 255F);
				colour = (int) ((colour << 8) + colourComponents[1] * 255F);
				colour = (int) ((colour << 8) + colourComponents[2] * 255F);
				
				
				baseColours.getTag().putInt("collar_colour", colour);
	            items.add(baseColours);
	        }
		}
	}
	
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTag();
		return nbttagcompound != null && nbttagcompound.contains("collar_colour", 3);
        
    }

    public int getColor(ItemStack stack) {
       
    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound != null)    
    		return nbttagcompound.getInt("collar_colour");

    	return 10511680;
      
    }

    public void removeColor(ItemStack stack) {

    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound != null)
    		nbttagcompound.remove("collar_colour");
        
    }

    public void setColor(ItemStack stack, int color) {

    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound == null) {	
    		nbttagcompound = new NBTTagCompound();
    		stack.setTag(nbttagcompound);
    	}
            
    	nbttagcompound.putInt("collar_colour", color);
    }
}
