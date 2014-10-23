package doggytalents.item;

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
import doggytalents.api.DogBedManager;

/**
 * @author ProPercivalalb
 */
public class ItemDogBed extends ItemBlock {

	public ItemDogBed(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List toolTipList, boolean extraDetail) {
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("doggytalents")) {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag("doggytalents");
		    
		    String woodId = tag.getString("woodId");
		    if(!Strings.isNullOrEmpty(woodId) && DogBedManager.isValidWoodId(woodId)) {
		    	ItemStack item = DogBedManager.getAssociactedWoodItem(woodId);
		    	toolTipList.add(item.getDisplayName() + " " + StatCollector.translateToLocal("dogBed.casing"));
		    }
		    else {
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woodError"));
		    }
		    	
		    String woolId = tag.getString("woolId");
		    if(!Strings.isNullOrEmpty(woolId) && DogBedManager.isValidWoolId(woolId)) {
		    	ItemStack item = DogBedManager.getAssociactedWoolItem(woolId);
		    	toolTipList.add(StatCollector.translateToLocal("dogBed.bedding." + woolId));	
		    }
		    
		    else {
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woolError"));
		    }
		}
	}

}
