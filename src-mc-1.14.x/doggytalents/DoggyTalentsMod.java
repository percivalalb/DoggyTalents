package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.lib.Reference;
import doggytalents.proxy.ClientProxy;
import doggytalents.proxy.CommonProxy;
import doggytalents.proxy.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * @author ProPercivalalb
 */
@Mod(value = Reference.MOD_ID)
public class DoggyTalentsMod {
    
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
    
    public static DoggyTalentsMod INSTANCE;
    public static CommonProxy PROXY;
    
    public DoggyTalentsMod() {
        INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }
}
