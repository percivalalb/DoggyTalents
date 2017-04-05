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

	public static Block dogBed;
	public static Block dogBath;
    public static Block foodBowl;
	
	public static void inti() {
		dogBed = new BlockDogBed().setUnlocalizedName("doggytalents.dogbed");
		dogBath = new BlockDogBath().setUnlocalizedName("doggytalents.dogbath");
		foodBowl = new BlockFoodBowl().setUnlocalizedName("doggytalents.foodbowl");
		
		GameRegistry.registerBlock(dogBed, ItemDogBed.class, new ResourceLocation(Reference.MOD_ID, "dog_bed").toString());
		GameRegistry.registerBlock(dogBath, new ResourceLocation(Reference.MOD_ID, "dog_bath").toString());
		GameRegistry.registerBlock(foodBowl, new ResourceLocation(Reference.MOD_ID, "food_bowl").toString());
		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
		
		dogBed.setHarvestLevel("axe", 0);
		dogBath.setHarvestLevel("pickaxe", 0);
		foodBowl.setHarvestLevel("pickaxe", 0);
	}
}
