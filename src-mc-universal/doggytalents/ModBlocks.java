package doggytalents;

import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.block.ItemDogBed;
import doggytalents.client.model.ModelHelper;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBath;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block DOG_BED;
	public static Block DOG_BATH;
    public static Block FOOD_BOWL;

	public static void onRegisterBlock(Object registry) {
		DoggyTalents.LOGGER.info("Registering Blocks");
		DOG_BED = VersionControl.createObject("BlockDogBedWrapper", BlockDogBed.class).setUnlocalizedName("doggytalents.dogbed").setRegistryName(Reference.MOD_ID + ":dog_bed");
		DOG_BATH = VersionControl.createObject("BlockDogBathWrapper", BlockDogBath.class).setUnlocalizedName("doggytalents.dogbath").setRegistryName(Reference.MOD_ID + ":dog_bath");
		FOOD_BOWL = VersionControl.createObject("BlockFoodBowlWrapper", BlockFoodBowl.class).setUnlocalizedName("doggytalents.foodbowl").setRegistryName(Reference.MOD_ID + ":food_bowl");

		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
		
		DOG_BED.setHarvestLevel("axe", 0);
		DOG_BATH.setHarvestLevel("pickaxe", 0);
		FOOD_BOWL.setHarvestLevel("pickaxe", 0);
		
		ObjectLib.METHODS.registerBlock(registry, DOG_BED);
		ObjectLib.METHODS.registerBlock(registry, DOG_BATH);
		ObjectLib.METHODS.registerBlock(registry, FOOD_BOWL);
	}
	
	public static void onRegisterItem(Object registry) {
		DoggyTalents.LOGGER.info("Registering ItemBlocks");
		ObjectLib.METHODS.registerItem(registry, VersionControl.createObject("ItemDogBedWrapper", ItemDogBed.class, Block.class, DOG_BED).setRegistryName(DOG_BED.getRegistryName()));
		ObjectLib.METHODS.registerItem(registry, makeItemBlock(DOG_BATH));
		ObjectLib.METHODS.registerItem(registry, makeItemBlock(FOOD_BOWL));
	}
	
	@SideOnly(Side.CLIENT)
	public static void setItemModels() {
		ModelHelper.setModel(DOG_BATH, 0, "doggytalents:dog_bath");
		ModelHelper.setModel(DOG_BED, 0, "doggytalents:dog_bed");
		ModelHelper.setModel(FOOD_BOWL, 0, "doggytalents:food_bowl");
	}
	
	private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
