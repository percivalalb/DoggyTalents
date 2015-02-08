package doggytalents.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;

/**
 * @author ProPercivalalb
 **/
public class ItemDT extends Item {
	
	private String path;
	
	public ItemDT(String iconPath) {
		super();
		this.setCreativeTab(DoggyTalentsMod.creativeTab);
		this.path = iconPath;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("doggytalents:" + path);
	}
}
