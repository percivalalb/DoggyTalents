package doggytalents.item;

import java.util.List;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import doggytalents.api.DogBedManager;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

/**
 * @author ProPercivalalb
 */
public class ItemDogBed extends ItemBlock {

	public ItemDogBed(int id) {
		super(id);
		this.setMaxStackSize(1);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List toolTipList, boolean extraDetail) {
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("doggytalents")) {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag("doggytalents");
		    	
		    String woodId = tag.getString("woodId");
		    if(!Strings.isNullOrEmpty(woodId) && DogBedManager.isValidWoodId(woodId)) {
		    	toolTipList.add(StatCollector.translateToLocal("dogBed.outside." + woodId));
		    }
		    else {
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woodError"));
		    }
		    	
		    String woolId = tag.getString("woolId");
		    if(!Strings.isNullOrEmpty(woolId) && DogBedManager.isValidWoolId(woolId)) {
		    	toolTipList.add(StatCollector.translateToLocal("dogBed.bedding." + woolId));	
		    }
		    
		    else {
		    	toolTipList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("dogBed.woolError"));
		    }
		}
	}

}
