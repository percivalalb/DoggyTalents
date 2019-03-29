package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.configuration.ConfigHandler;
import doggytalents.handler.GuiHandler;
import doggytalents.lib.Reference;
import doggytalents.proxy.ClientProxy;
import doggytalents.proxy.ServerProxy;
import doggytalents.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * @author ProPercivalalb
 */
@Mod(value = Reference.MOD_ID)
public class DoggyTalentsMod {
	
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

	public static DoggyTalentsMod INSTANCE;
	public static CommonProxy PROXY;
	
    public static IForgeRegistry<BedMaterial> CASING;
    
	public DoggyTalentsMod() {
		INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        ConfigHandler.init();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
        
        CASING = makeRegistry(new ResourceLocation("doggytalents:casing"), BedMaterial.class, 256).create();
	}
	
	private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type, int max) {
		return new RegistryBuilder<T>().setName(name).setType(type).setMaxID(max);
	}
}
