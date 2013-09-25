package doggytalents;

import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.item.ItemDogBed;
import doggytalents.lib.BlockIds;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block dogBed;
    public static Block foodBowl;
	
	public static void inti() {
		dogBed = new BlockDogBed(BlockIds.ID_DOG_BED).setUnlocalizedName("dt.dogBed").setCreativeTab(DoggyTalentsMod.creativeTab);
		foodBowl = new BlockFoodBowl(BlockIds.ID_DOG_BOWL).setUnlocalizedName("dt.foodBowl");
		
		GameRegistry.registerBlock(dogBed, ItemDogBed.class, "dt.dogBed");
		GameRegistry.registerBlock(foodBowl, "dt.foodBowl");
		GameRegistry.registerTileEntity(TileEntityDogBed.class, "dt.dogBed");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "dt.dogBowl");
	}
}
