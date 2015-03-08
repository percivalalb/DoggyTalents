package doggytalents.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.api.DoggyTalentsAPI;

/**
 * @author ProPercivalalb
 **/
public class ItemDT extends Item {
	
	private String iconPath;
	
	public ItemDT(String iconPath) {
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
		this.iconPath = iconPath;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("doggytalents:" + this.iconPath);
	}
}
