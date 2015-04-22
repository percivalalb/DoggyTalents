package doggytalents;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.block.ItemDogBed;
import doggytalents.tileentity.TileEntityDogBath;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block dogBed;
	public static Block dogBath;
    public static Block foodBowl;
	
	public static void inti() {
		dogBed = new BlockDogBed().setUnlocalizedName("doggytalents.dogbed");
		dogBath = new BlockDogBath().setUnlocalizedName("doggytalents.dogbath");
		foodBowl = new BlockFoodBowl().setUnlocalizedName("doggytalents.foodbowl");
		
		GameRegistry.registerBlock(dogBed, ItemDogBed.class, "dog_bed");
		GameRegistry.registerBlock(dogBath, "dog_bath");
		GameRegistry.registerBlock(foodBowl, "food_bowl");
		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
	}
}
