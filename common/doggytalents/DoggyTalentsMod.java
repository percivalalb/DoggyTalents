package doggytalents;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.common.base.Strings;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import doggytalents.api.DefaultBedMaterial;
import doggytalents.api.DogBedManager;
import doggytalents.core.addon.AddonManager;
import doggytalents.core.handler.ConfigurationHandler;
import doggytalents.core.handler.ConnectionHandler;
import doggytalents.core.handler.EventRightClickEntity;
import doggytalents.core.handler.LocalizationHandler;
import doggytalents.core.helper.LogHelper;
import doggytalents.core.helper.VersionHelper;
import doggytalents.core.helper.VersionHelper.Type;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;

/**
 * @author ProPercivalalb
 * The Main Mod Class.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDENCIES)
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class DoggyTalentsMod {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalentsMod instance;
	
	@SidedProxy(clientSide = Reference.SP_CLIENT, serverSide = Reference.SP_SERVER)
    public static CommonProxy proxy;
	
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
		this.creativeTab = new CreativeTabDoggyTalents();
		VersionHelper.checkVersion(Type.BLANK);
		//Loads the Languages into the game
		LocalizationHandler.loadLanguages();
		//Loads the Blocks/Items
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
		proxy.onPreLoad();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
	    NetworkRegistry.instance().registerChannel(new PacketHandler(), Reference.CHANNEL_NAME);
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		//Handlers
		MinecraftForge.EVENT_BUS.register(new EventRightClickEntity());
		proxy.registerHandlers();
	}
		
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent par1) {
		DogBedManager.registerBedWood("oakPlank", new DefaultBedMaterial(Block.planks, 0), new ItemStack(Block.planks, 1, 0));
		DogBedManager.registerBedWood("sprucePlank", new DefaultBedMaterial(Block.planks, 1), new ItemStack(Block.planks, 1, 1));
		DogBedManager.registerBedWood("birchPlank", new DefaultBedMaterial(Block.planks, 2), new ItemStack(Block.planks, 1, 2));
		DogBedManager.registerBedWood("junglePlank", new DefaultBedMaterial(Block.planks, 3), new ItemStack(Block.planks, 1, 3));
		
		DogBedManager.registerBedWool("whiteWool", new DefaultBedMaterial(Block.cloth, 0), new ItemStack(Block.cloth, 1, 0));
		DogBedManager.registerBedWool("orangeWool", new DefaultBedMaterial(Block.cloth, 1), new ItemStack(Block.cloth, 1, 1));
		DogBedManager.registerBedWool("magentaWool", new DefaultBedMaterial(Block.cloth, 2), new ItemStack(Block.cloth, 1, 2));
		DogBedManager.registerBedWool("lightBlueWool", new DefaultBedMaterial(Block.cloth, 3), new ItemStack(Block.cloth, 1, 3));
		DogBedManager.registerBedWool("yellowWool", new DefaultBedMaterial(Block.cloth, 4), new ItemStack(Block.cloth, 1, 4));
		DogBedManager.registerBedWool("limeWool", new DefaultBedMaterial(Block.cloth, 5), new ItemStack(Block.cloth, 1, 5));
		DogBedManager.registerBedWool("pinkWool", new DefaultBedMaterial(Block.cloth, 6), new ItemStack(Block.cloth, 1, 6));
		DogBedManager.registerBedWool("grayWool", new DefaultBedMaterial(Block.cloth, 7), new ItemStack(Block.cloth, 1, 7));
		DogBedManager.registerBedWool("lightGrayWool", new DefaultBedMaterial(Block.cloth, 8), new ItemStack(Block.cloth, 1, 8));
		DogBedManager.registerBedWool("cyanWool", new DefaultBedMaterial(Block.cloth, 9), new ItemStack(Block.cloth, 1, 9));
		DogBedManager.registerBedWool("purpleWool", new DefaultBedMaterial(Block.cloth, 10), new ItemStack(Block.cloth, 1, 10));
		DogBedManager.registerBedWool("blueWool", new DefaultBedMaterial(Block.cloth, 11), new ItemStack(Block.cloth, 1, 11));
		DogBedManager.registerBedWool("brownWool", new DefaultBedMaterial(Block.cloth, 12), new ItemStack(Block.cloth, 1, 12));
		DogBedManager.registerBedWool("greenWool", new DefaultBedMaterial(Block.cloth, 13), new ItemStack(Block.cloth, 1, 13));
		DogBedManager.registerBedWool("redWool", new DefaultBedMaterial(Block.cloth, 14), new ItemStack(Block.cloth, 1, 14));
		DogBedManager.registerBedWool("blackWool", new DefaultBedMaterial(Block.cloth, 15), new ItemStack(Block.cloth, 1, 15));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.throwBone, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.slimeBall});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.throwBone, 1, 0), new Object[] {new ItemStack(ModItems.throwBone, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.superTreat, 5), new Object[] { new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(Item.appleGold, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.direTreat, 1), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(Block.whiteStone, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.breedingBone, 2), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(Item.beefCooked, 1), new ItemStack(Item.porkCooked, 1), new ItemStack(Item.chickenCooked, 1), new ItemStack(Item.fishCooked, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.masterTreat, 5), new Object[] {new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(Item.diamond, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.trainingTreat, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Item.silk, 'U', Item.bone, 'V', Item.gunpowder, 'X', Item.sugar, 'Y', Item.wheat});
        GameRegistry.addRecipe(new ItemStack(ModItems.collarShears, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.shears});
        GameRegistry.addRecipe(new ItemStack(ModItems.commandEmblem, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.ingotGold, 'Y', Item.bow});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.foodBowl, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Item.ingotIron, 'Y', Item.bone});
		
		AddonManager.registerAddons();
		AddonManager.runRegisteredAddons(ConfigurationHandler.configuration);
	}
}
