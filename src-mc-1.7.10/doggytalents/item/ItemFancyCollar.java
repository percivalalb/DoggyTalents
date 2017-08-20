package doggytalents.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFancyCollar extends ItemDT {

	public static final int NO_COLLAR = 3;
	private IIcon[] collarIcons;	
	
	public ItemFancyCollar() {
		super("error");
		this.setHasSubtypes(true);
		this.collarIcons = new IIcon[NO_COLLAR];
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return stack.getMetadata() == 0 ? EnumRarity.epic : super.getRarity(stack);
    }
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + par1ItemStack.getMetadata();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		for(int i = 0; i < this.NO_COLLAR; i++)
			this.collarIcons[i] = par1IconRegister.registerIcon("doggytalents:fancy_collar_" + i);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for(int i = 0; i < this.NO_COLLAR; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		if(meta >= 0 && meta < NO_COLLAR)
			return this.collarIcons[meta];

		return null;
    }
}
