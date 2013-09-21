package doggytalents;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
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
import doggytalents.core.handler.ConfigurationHandler;
import doggytalents.core.handler.ConnectionHandler;
import doggytalents.core.handler.LocalizationHandler;
import doggytalents.core.helper.LogHelper;
import doggytalents.core.helper.VersionHelper;
import doggytalents.core.helper.VersionHelper.Type;
import doggytalents.core.proxy.CommonProxy;
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
	
	public DoggyTalentsMod() {
   	 	instance = this;
    }
	
	@EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.onPreLoad();
		ConfigurationHandler.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		
		VersionHelper.checkVersion(Type.BLANK);
		//Loads the Languages into the game
		LocalizationHandler.loadLanguages();
		//Loads the Blocks/Items
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
	    NetworkRegistry.instance().registerChannel(new PacketHandler(), Reference.CHANNEL_NAME);
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		//Handlers
		//MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
		proxy.registerHandlers();
	}
		
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent par1) {
		
	}
}
