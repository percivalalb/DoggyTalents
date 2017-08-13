package doggytalents.base.b;

import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.base.ObjectLib;
import doggytalents.block.ItemDogBed;
import doggytalents.lib.TextFormatting;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.8.9 to 1.11.2 Code
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
		    	tooltip.add(ObjectLib.BRIDGE.translateToLocal(DogBedRegistry.CASINGS.getLookUpValue(casingId)));
		    else
		    	tooltip.add(TextFormatting.RED + ObjectLib.BRIDGE.translateToLocal("dogBed.woodError"));
		    	
		    if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    	tooltip.add(ObjectLib.BRIDGE.translateToLocal(DogBedRegistry.BEDDINGS.getLookUpValue(beddingId)));	
		    else
		    	tooltip.add(TextFormatting.RED + ObjectLib.BRIDGE.translateToLocal("dogBed.woolError"));
		}
	}
}
