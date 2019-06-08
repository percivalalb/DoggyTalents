package doggytalents.configuration;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.DoggyTalentsMod;
import doggytalents.lib.Constants;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigHandler::modConfig);
	}
	
	public static void modConfig(final ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if(config.getSpec() == ConfigHandler.CONFIG_CLIENT_SPEC) {
        	ConfigHandler.refreshClient();
        } else if(config.getSpec() == ConfigHandler.CONFIG_COMMON_SPEC) {
        	ConfigHandler.refreshCommon();
        }
    }
	
	public static void refreshCommon() {
		DoggyTalentsMod.LOGGER.debug("Refresh Common Config");
		Constants.DOGS_IMMORTAL = COMMON.DOGS_IMMORTAL.get();
		Constants.TEN_DAY_PUPS = COMMON.TEN_DAY_PUPS.get();
		Constants.IS_HUNGER_ON = COMMON.IS_HUNGER_ON.get();
		Constants.STARTING_ITEMS = COMMON.STARTING_ITEMS.get();
		Constants.DOG_GENDER = COMMON.DOG_GENDER.get();
		Constants.DOG_WHINE_WHEN_HUNGER_LOW = COMMON.DOG_WHINE_WHEN_HUNGER_LOW.get();
		Constants.PUPS_GET_PARENT_LEVELS = COMMON.PUPS_GET_PARENT_LEVELS.get();
	}
	
	public static void refreshClient() {
		DoggyTalentsMod.LOGGER.debug("Refresh Client Config");
		Constants.DIRE_PARTICLES = CLIENT.DIRE_PARTICLES.get();
		Constants.RENDER_BLOOD = CLIENT.RENDER_BLOOD.get();
		Constants.DOGGY_WINGS = CLIENT.DOGGY_WINGS.get();
		Constants.DOGGY_CHEST = CLIENT.DOGGY_CHEST.get();
		Constants.DOGGY_SADDLE = CLIENT.DOGGY_SADDLE.get();
		Constants.USE_DT_TEXTURES = CLIENT.USE_DT_TEXTURES.get();
		Constants.DOGGY_ARMOUR = CLIENT.DOGGY_ARMOUR.get();
	}
}
