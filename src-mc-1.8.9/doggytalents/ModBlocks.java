package doggytalents;

import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.block.ItemDogBed;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBath;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block DOG_BED;
	public static Block DOG_BATH;
    public static Block FOOD_BOWL;

	public static void init() {
		DoggyTalents.LOGGER.info("Registering Blocks");
		DOG_BED = new BlockDogBed().setUnlocalizedName("doggytalents.dogbed").setRegistryName(Reference.MOD_ID + ":dog_bed");
		DOG_BATH = new BlockDogBath().setUnlocalizedName("doggytalents.dogbath").setRegistryName(Reference.MOD_ID + ":dog_bath");
		FOOD_BOWL = new BlockFoodBowl().setUnlocalizedName("doggytalents.foodbowl").setRegistryName(Reference.MOD_ID + ":food_bowl");

		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
		
		DOG_BED.setHarvestLevel("axe", 0);
		DOG_BATH.setHarvestLevel("pickaxe", 0);
		FOOD_BOWL.setHarvestLevel("pickaxe", 0);
		
		GameRegistry.registerBlock(DOG_BED, ItemDogBed.class);
		GameRegistry.registerBlock(DOG_BATH);
		GameRegistry.registerBlock(FOOD_BOWL);
	}
}
