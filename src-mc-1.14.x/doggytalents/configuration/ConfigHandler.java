package doggytalents.configuration;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigHandler {

	public static DTCommonConfig COMMON;
	public static DTClientConfig CLIENT;
    public static ForgeConfigSpec CONFIG_COMMON_SPEC;
    public static ForgeConfigSpec CONFIG_CLIENT_SPEC;
    
	public static void init() {
        Pair<DTCommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(DTCommonConfig::new);
        CONFIG_COMMON_SPEC = commonPair.getRight();
        COMMON = commonPair.getLeft();
        Pair<DTClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(DTClientConfig::new);
        CONFIG_CLIENT_SPEC = clientPair.getRight();
        CLIENT = clientPair.getLeft();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_CLIENT_SPEC);
	}
}
