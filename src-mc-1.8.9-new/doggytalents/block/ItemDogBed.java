package doggytalents.block;

import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    
		    String casingId = tag.getString("casingId");
		    String beddingId = tag.getString("beddingId");
		    
		    if(DogBedRegistry.CASINGS.isValidId(casingId))
		    	toolTipList.add(I18n.format(DogBedRegistry.CASINGS.getLookUpValue(casingId)));
		    else
		    	toolTipList.add(EnumChatFormatting.RED + I18n.format("dogBed.woodError"));
		    	
		    if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    	toolTipList.add(I18n.format(DogBedRegistry.BEDDINGS.getLookUpValue(beddingId)));	
		    else
		    	toolTipList.add(EnumChatFormatting.RED + I18n.format("dogBed.woolError"));
		}
	}

}
