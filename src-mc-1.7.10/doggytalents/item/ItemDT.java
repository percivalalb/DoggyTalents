package doggytalents.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalents;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 **/
public class ItemDT extends Item {
	
	private String iconPath;
	
	public ItemDT(String iconPath) {
		this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
		this.iconPath = iconPath;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.iconPath);
	}
}
