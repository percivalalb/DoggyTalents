package doggytalents.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCreativeOwnerChange extends ItemDT {

	public ItemCreativeOwnerChange() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		
		tooltip.add(new TextComponentTranslation(this.getUnlocalizedName() + ".tooltip").getFormattedText());
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
