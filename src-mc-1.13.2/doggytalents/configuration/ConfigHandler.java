package doggytalents.configuration;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigHandler {

	public static DTConfig CONFIG;
    public static ForgeConfigSpec CONFIG_SPEC;
	
	public static void init() {
        Pair<DTConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(DTConfig::new);
        CONFIG_SPEC = commonPair.getRight();
        CONFIG = commonPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
	}
}
