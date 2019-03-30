package doggytalents;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.event.BeddingRegistryEvent;
import doggytalents.lib.BlockNames;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

	@ObjectHolder(BlockNames.DOG_BED)
	public static Block DOG_BED;
	@ObjectHolder(BlockNames.DOG_BATH)
	public static Block DOG_BATH;
	@ObjectHolder(BlockNames.FOOD_BOWL)
    public static Block FOOD_BOWL;
	
	@ObjectHolder(BlockNames.DOG_BED)
	public static TileEntityType<TileEntityDogBed> TILE_DOG_BED;
	@ObjectHolder(BlockNames.FOOD_BOWL)
	public static TileEntityType<TileEntityFoodBowl> TILE_FOOD_BOWL;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

	    @SubscribeEvent
	    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
	    	IForgeRegistry<Block> blockRegistry = event.getRegistry();
	        MinecraftForge.EVENT_BUS.post(new BeddingRegistryEvent());
	        
	        
	        DoggyTalentsMod.LOGGER.info("Registering Blocks");
	        blockRegistry.register(new BlockDogBed());
	        blockRegistry.register(new BlockDogBath());
	        blockRegistry.register(new BlockFoodBowl());
	    }
	    
	    @SubscribeEvent
	    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event){
	    	IForgeRegistry<TileEntityType<?>> tileRegistry = event.getRegistry();
	    	
	        tileRegistry.register(register(BlockNames.DOG_BED, TileEntityType.Builder.create(TileEntityDogBed::new)).setRegistryName(BlockNames.DOG_BED));
	        tileRegistry.register(register(BlockNames.FOOD_BOWL, TileEntityType.Builder.create(TileEntityFoodBowl::new)).setRegistryName(BlockNames.FOOD_BOWL));
	    }
	    
	    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder) {
	        Type<?> type = null;

	        try {
	           type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1631)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
	        } 
	        catch(IllegalArgumentException illegalstateexception) {
	           if(SharedConstants.developmentMode) {
	              throw illegalstateexception;
	           }

	           DoggyTalentsMod.LOGGER.warn("No data fixer registered for block entity {}", (Object)id);
	        }

	        TileEntityType<T> tileentitytype = builder.build(type);
	        return tileentitytype;
		}
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	    	DoggyTalentsMod.LOGGER.info("Registering ItemBlocks");
	    	event.getRegistry().register(makeItemBlock(DOG_BED, ModCreativeTabs.DOG_BED));
	    	event.getRegistry().register(makeItemBlock(DOG_BATH));
	    	event.getRegistry().register(makeItemBlock(FOOD_BOWL));
	    }
	    
	    private static ItemBlock makeItemBlock(Block block) {
	    	return makeItemBlock(block, ModCreativeTabs.GENERAL);
	    }
	    
	    private static ItemBlock makeItemBlock(Block block, ItemGroup group) {
	        return (ItemBlock)new ItemBlock(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
	    }
    }
}