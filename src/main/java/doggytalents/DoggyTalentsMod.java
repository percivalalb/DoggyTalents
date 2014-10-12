package doggytalents;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.api.DefaultBedMaterial;
import doggytalents.api.DogBedManager;
import doggytalents.core.addon.AddonManager;
import doggytalents.core.handler.ConfigurationHandler;
import doggytalents.core.handler.ConnectionHandler;
import doggytalents.core.handler.EventRightClickEntity;
import doggytalents.core.helper.VersionHelper;
import doggytalents.core.helper.VersionHelper.UpdateType;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import doggytalents.lib.Reference;
import doggytalents.network.NetworkManager;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * @author ProPercivalalb
 * The Main Mod Class.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDENCIES)
public class DoggyTalentsMod {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalentsMod instance;
	
	@SidedProxy(clientSide = Reference.SP_CLIENT, serverSide = Reference.SP_SERVER)
    public static CommonProxy proxy;
	
	public static NetworkManager NETWORK_MANAGER;
	
	public static CreativeTabs creativeTab;
	
	public DoggyTalentsMod() {
   	 	instance = this;
    }
	
	@EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
		
    }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		creativeTab = new CreativeTabDoggyTalents();
		VersionHelper.checkVersion(UpdateType.BLANK);
		//Loads the Blocks/Items
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
		proxy.onPreLoad();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		NETWORK_MANAGER = new NetworkManager();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		FMLCommonHandler.instance().bus().register(new ConnectionHandler());
		//Handlers
		MinecraftForge.EVENT_BUS.register(new EventRightClickEntity());
		proxy.registerHandlers();
	}
		
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {
		ArrayList<ItemStack> planks = OreDictionary.getOres("plankWood");
		
		for(ItemStack plank : planks) {
			if (plank.getHasSubtypes()) {
				Item plankItem = plank.getItem();
				ArrayList<ItemStack> plankSubTypes = new ArrayList<ItemStack>();
				plankItem.getSubItems(plankItem, null, plankSubTypes);
				for (ItemStack subPlank : plankSubTypes)
					DogBedManager.registerBedWood(subPlank);
			} 
			else
				DogBedManager.registerBedWood(plank);
		}

		DogBedManager.registerBedWool("whiteWool", new DefaultBedMaterial(Blocks.wool, 0), new ItemStack(Blocks.wool, 1, 0));
		DogBedManager.registerBedWool("orangeWool", new DefaultBedMaterial(Blocks.wool, 1), new ItemStack(Blocks.wool, 1, 1));
		DogBedManager.registerBedWool("magentaWool", new DefaultBedMaterial(Blocks.wool, 2), new ItemStack(Blocks.wool, 1, 2));
		DogBedManager.registerBedWool("lightBlueWool", new DefaultBedMaterial(Blocks.wool, 3), new ItemStack(Blocks.wool, 1, 3));
		DogBedManager.registerBedWool("yellowWool", new DefaultBedMaterial(Blocks.wool, 4), new ItemStack(Blocks.wool, 1, 4));
		DogBedManager.registerBedWool("limeWool", new DefaultBedMaterial(Blocks.wool, 5), new ItemStack(Blocks.wool, 1, 5));
		DogBedManager.registerBedWool("pinkWool", new DefaultBedMaterial(Blocks.wool, 6), new ItemStack(Blocks.wool, 1, 6));
		DogBedManager.registerBedWool("grayWool", new DefaultBedMaterial(Blocks.wool, 7), new ItemStack(Blocks.wool, 1, 7));
		DogBedManager.registerBedWool("lightGrayWool", new DefaultBedMaterial(Blocks.wool, 8), new ItemStack(Blocks.wool, 1, 8));
		DogBedManager.registerBedWool("cyanWool", new DefaultBedMaterial(Blocks.wool, 9), new ItemStack(Blocks.wool, 1, 9));
		DogBedManager.registerBedWool("purpleWool", new DefaultBedMaterial(Blocks.wool, 10), new ItemStack(Blocks.wool, 1, 10));
		DogBedManager.registerBedWool("blueWool", new DefaultBedMaterial(Blocks.wool, 11), new ItemStack(Blocks.wool, 1, 11));
		DogBedManager.registerBedWool("brownWool", new DefaultBedMaterial(Blocks.wool, 12), new ItemStack(Blocks.wool, 1, 12));
		DogBedManager.registerBedWool("greenWool", new DefaultBedMaterial(Blocks.wool, 13), new ItemStack(Blocks.wool, 1, 13));
		DogBedManager.registerBedWool("redWool", new DefaultBedMaterial(Blocks.wool, 14), new ItemStack(Blocks.wool, 1, 14));
		DogBedManager.registerBedWool("blackWool", new DefaultBedMaterial(Blocks.wool, 15), new ItemStack(Blocks.wool, 1, 15));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.throwBone, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.bone, 'Y', Items.slime_ball});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.throwBone, 1, 0), new Object[] {new ItemStack(ModItems.throwBone, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.superTreat, 5), new Object[] { new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(Items.golden_apple, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.direTreat, 1), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(Blocks.end_stone, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.breedingBone, 2), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(Items.cooked_beef, 1), new ItemStack(Items.cooked_porkchop, 1), new ItemStack(Items.cooked_chicken, 1), new ItemStack(Items.cooked_fished, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.masterTreat, 5), new Object[] {new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(Items.diamond, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.trainingTreat, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Items.string, 'U', Items.bone, 'V', Items.gunpowder, 'X', Items.sugar, 'Y', Items.wheat});
        GameRegistry.addRecipe(new ItemStack(ModItems.collarShears, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.bone, 'Y', Items.shears});
        GameRegistry.addRecipe(new ItemStack(ModItems.commandEmblem, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.gold_ingot, 'Y', Items.bow});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.foodBowl, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.bone});
		
		AddonManager.registerAddons();
		AddonManager.runRegisteredAddons(ConfigurationHandler.configuration);
	}
}
