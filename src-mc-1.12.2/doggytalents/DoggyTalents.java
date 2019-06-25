package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.addon.AddonManager;
import doggytalents.configuration.ConfigurationHandler;
import doggytalents.lib.Reference;
import doggytalents.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, updateJSON = Reference.UPDATE_URL, guiFactory = Reference.GUI_FACTORY, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.ACCEPTED_MC_VERSION)
public class DoggyTalents {

    @Instance(value = Reference.MOD_ID)
    public static DoggyTalents INSTANCE;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy PROXY;
    
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AddonManager.registerAddons();
        AddonManager.runRegisteredAddons(ConfigurationHandler.CONFIG);
        PROXY.postInit(event);
    }
}
