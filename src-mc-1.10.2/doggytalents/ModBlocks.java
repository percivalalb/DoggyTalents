package doggytalents;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@EventBusSubscriber
public class ModBlocks {

	public static Block DOG_BED;
	public static Block DOG_BATH;
    public static Block FOOD_BOWL;

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		DOG_BED = new BlockDogBed().setUnlocalizedName("doggytalents.dogbed").setRegistryName(Reference.MOD_ID + ":dog_bed");
		DOG_BATH = new BlockDogBath().setUnlocalizedName("doggytalents.dogbath").setRegistryName(Reference.MOD_ID + ":dog_bath");
		FOOD_BOWL = new BlockFoodBowl().setUnlocalizedName("doggytalents.foodbowl").setRegistryName(Reference.MOD_ID + ":food_bowl");

		GameRegistry.registerTileEntity(TileEntityDogBed.class, "doggytalents:dog_bed");
		GameRegistry.registerTileEntity(TileEntityDogBath.class, "doggytalents:dog_bath");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "doggytalents:dog_bowl");
		
		DOG_BED.setHarvestLevel("axe", 0);
		DOG_BATH.setHarvestLevel("pickaxe", 0);
		FOOD_BOWL.setHarvestLevel("pickaxe", 0);
		
		event.getRegistry().register(ModBlocks.DOG_BED);
	    event.getRegistry().register(ModBlocks.DOG_BATH);
	    event.getRegistry().register(ModBlocks.FOOD_BOWL);
	}
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemDogBed(DOG_BED).setRegistryName(DOG_BED.getRegistryName()));
	    event.getRegistry().register(makeItemBlock(DOG_BATH));
	    event.getRegistry().register(makeItemBlock(FOOD_BOWL));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModelHelper.setModel(ModBlocks.DOG_BATH, 0, "doggytalents:dog_bath");
		ModelHelper.setModel(ModBlocks.DOG_BED, 0, "doggytalents:dog_bed");
		ModelHelper.setModel(ModBlocks.FOOD_BOWL, 0, "doggytalents:food_bowl");
	}
	
	private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
