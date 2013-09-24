package doggytalents.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 **/
public class ItemDT extends Item {
	
	private String path;
	
	public ItemDT(int id, String iconPath) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabFood);
		this.path = iconPath;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("doggytalents:" + path);
	}
}
