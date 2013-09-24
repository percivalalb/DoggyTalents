package doggytalents;

import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.block.BlockDogBed;
import doggytalents.item.ItemDogBed;
import doggytalents.lib.BlockIds;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block dogBed;
	
	public static void inti() {
		dogBed = new BlockDogBed(BlockIds.ID_DOG_BED).setUnlocalizedName("dt.dogBed").setCreativeTab(CreativeTabs.tabBlock);
		
		GameRegistry.registerBlock(dogBed, ItemDogBed.class, "dt.dogBed");
		GameRegistry.registerTileEntity(TileEntityDogBed.class, "dt.dogBed");
	}
}
