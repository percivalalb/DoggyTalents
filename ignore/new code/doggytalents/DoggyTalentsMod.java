package doggytalents;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
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
import doggytalents.core.handler.ActionHandler;
import doggytalents.core.handler.LocalizationHandler;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DoggyTalentsMod {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalentsMod instance;
	
	@SidedProxy(clientSide = Reference.SP_CLIENT, serverSide = Reference.SP_SERVER)
    public static CommonProxy proxy;
	
	public DoggyTalentsMod() {
   	 	instance = this;
    }
	
	@ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
        //Initialize the custom commands
        //CommandHandler.initCommands(event);
    }
	
	@PreInit
	public void preInit(FMLPreInitializationEvent par1) {
		
		//Loads the Languages into the game
		LocalizationHandler.loadLanguages();
		
		//Initialise the Blocks
		ModBlocks.init();
		//Initialise the Items
		ModItems.init();
		//Initialise the Entitys
		ModEntitys.init();
		
		//For Client use only
		proxy.registerRenders();
	}
	
	@Init
	public void init(FMLInitializationEvent par1) {
		NetworkRegistry.instance().registerChannel(new PacketHandler(), Reference.CHANNEL_NAME);
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		
		//Registers the Action Handler
		MinecraftForge.EVENT_BUS.register(new ActionHandler());
	}
		
	@PostInit
	public void modsLoaded(FMLPostInitializationEvent par1) {
		
	}
}
