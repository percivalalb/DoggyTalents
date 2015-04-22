package doggytalents.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.helper.LogHelper;

/**
 * @author ProPercivalalb
 */
public class ItemDogBed extends ItemBlock {

	public ItemDogBed(Block block) {
		super(block);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List toolTipList, boolean extraDetail) {
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("doggytalents")) {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag("doggytalents");
		    
		    String casingId = tag.getString("casingId");
		    String beddingId = tag.getString("beddingId");
		    
		    if(DogBedRegistry.CASINGS.isValidId(casingId))
		    	toolTipList.add(StatCollector.translateToLocal(DogBedRegistry.CASINGS.getLookUpValue(casingId)));
		    else
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woodError"));
		    	
		    if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    	toolTipList.add(StatCollector.translateToLocal(DogBedRegistry.BEDDINGS.getLookUpValue(beddingId)));	
		    else
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woolError"));
		}
	}

}
