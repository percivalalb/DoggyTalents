package doggytalents.base.c;

import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.ItemDogBed;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.11.2 Code
 */
public class ItemDogBedWrapper extends ItemDogBed {

	public ItemDogBedWrapper(Block block) {
		super(block);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    
		    String casingId = tag.getString("casingId");
		    String beddingId = tag.getString("beddingId");
		    
		    if(DogBedRegistry.CASINGS.isValidId(casingId))
		    	tooltip.add(I18n.translateToLocal(DogBedRegistry.CASINGS.getLookUpValue(casingId)));
		    else
		    	tooltip.add(TextFormatting.RED + I18n.translateToLocal("dogBed.woodError"));
		    	
		    if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    	tooltip.add(I18n.translateToLocal(DogBedRegistry.BEDDINGS.getLookUpValue(beddingId)));	
		    else
		    	tooltip.add(TextFormatting.RED + I18n.translateToLocal("dogBed.woolError"));
		}
	}
}
