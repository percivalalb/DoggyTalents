package doggytalents.item;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.helper.DogUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemCapeColoured extends Item {
	
	protected static int[] WHITE = new int[] {0,0,0};
	
	public ItemCapeColoured(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		int[] rgb = WHITE;
		if(stack.hasTag() && stack.getTag().contains("cap_colour")) {
			rgb = DogUtil.rgbIntToIntArray(stack.getTag().getInt("cap_colour"));
		}

		tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".tooltip", TextFormatting.RED + "" + rgb[0] + TextFormatting.GREEN + " " + rgb[1] + TextFormatting.BLUE + " " + rgb[2]));
	}
	
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTag();
		return nbttagcompound != null && nbttagcompound.contains("cape_colour", 3);
        
    }

    public int getColor(ItemStack stack) {
       
    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound != null)    
    		return nbttagcompound.getInt("cape_colour");

    	return 10511680;
      
    }

    public void removeColor(ItemStack stack) {

    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound != null)
    		nbttagcompound.remove("cape_colour");
        
    }

    public void setColor(ItemStack stack, int color) {

    	NBTTagCompound nbttagcompound = stack.getTag();

    	if(nbttagcompound == null) {	
    		nbttagcompound = new NBTTagCompound();
    		stack.setTag(nbttagcompound);
    	}
            
    	nbttagcompound.putInt("cape_colour", color);
    }
}
