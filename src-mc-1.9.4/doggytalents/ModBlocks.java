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
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {

	public static Block DOG_BED;
	public static Block DOG_BATH;
    public static Block FOOD_BOWL;
	
	public static void inti() {
		DOG_BED = new BlockDogBed().setUnlocalizedName("doggytalents.dogbed");
		DOG_BATH = new BlockDogBath().setUnlocalizedName("doggytalents.dogbath");
		FOOD_BOWL = new BlockFoodBowl().setUnlocalizedName("doggytalents.foodbowl");
		
		GameRegistry.register(DOG_BED, new ResourceLocation(Reference.MOD_ID, "dog_bed"));
		GameRegistry.register(new ItemDogBed(DOG_BED), new ResourceLocation(Reference.MOD_ID, "dog_bed"));
		GameRegistry.register(DOG_BATH, new ResourceLocation(Reference.MOD_ID, "dog_bath"));
		GameRegistry.register(new ItemBlock(DOG_BATH), new ResourceLocation(Reference.MOD_ID, "dog_bath"));
		GameRegistry.register(FOOD_BOWL, new ResourceLocation(Reference.MOD_ID, "food_bowl"));
		GameRegistry.register(new ItemBlock(FOOD_BOWL), new ResourceLocation(Reference.MOD_ID, "food_bowl"));
		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
		
		DOG_BED.setHarvestLevel("axe", 0);
		DOG_BATH.setHarvestLevel("pickaxe", 0);
		FOOD_BOWL.setHarvestLevel("pickaxe", 0);
	}
}
