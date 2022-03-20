package doggytalents.common.config;

import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {

    public static ClientConfig CLIENT;
    public static ServerConfig SERVER;
    public static TalentConfig TALENT;
    private static ForgeConfigSpec CONFIG_SERVER_SPEC;
    private static ForgeConfigSpec CONFIG_CLIENT_SPEC;
    private static ForgeConfigSpec CONFIG_TALENT_SPEC;

    public static final boolean ALWAYS_SHOW_DOG_NAME = true;
    public static final float DEFAULT_MAX_HUNGER = 120F;
    public static final boolean SEND_SKIN = false;
    public static final boolean DISPLAY_OTHER_DOG_SKINS = false;
    public static final boolean WHISTLE_SOUNDS = true;

    public static void init(IEventBus modEventBus) {
        Pair<ServerConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        CONFIG_SERVER_SPEC = commonPair.getRight();
        SERVER = commonPair.getLeft();
        Pair<ClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CONFIG_CLIENT_SPEC = clientPair.getRight();
        CLIENT = clientPair.getLeft();
        DoggyTalents2.LOGGER.debug("Register configs");

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_CLIENT_SPEC);
    }

    public static void initTalentConfig() {
        Pair<TalentConfig, ForgeConfigSpec> talentPair = new ForgeConfigSpec.Builder().configure(TalentConfig::new);
        CONFIG_TALENT_SPEC = talentPair.getRight();
        TALENT = talentPair.getLeft();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_TALENT_SPEC, "doggytalents-talents.toml");
    }

    public static class ClientConfig {

        public ForgeConfigSpec.BooleanValue DIRE_PARTICLES;
        public ForgeConfigSpec.BooleanValue RENDER_CHEST;
        public ForgeConfigSpec.BooleanValue USE_DT_TEXTURES;
        public ForgeConfigSpec.BooleanValue RENDER_ARMOUR;
        public ForgeConfigSpec.BooleanValue RENDER_SADDLE;
        public ForgeConfigSpec.BooleanValue RENDER_WINGS;
        public ForgeConfigSpec.BooleanValue RENDER_BLOOD;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            builder.pop();
            builder.push("Dog Render");

            DIRE_PARTICLES = builder
                    .comment("Enables the particle effect on Dire Level 30 dogs.")
                    .translation("doggytalents.config.client.enable_dire_particles")
                    .define("enable_dire_particles", true);
            RENDER_CHEST = builder
                    .comment("When enabled, dogs with points in pack puppy will have chests on their side.")
                    .translation("doggytalents.config.client.render_chest")
                    .define("render_chest", true);
            USE_DT_TEXTURES = builder
                    .comment("If disabled will use the default minecraft wolf skin for all dog textures.")
                    .translation("doggytalents.config.client.enable_dt_textures")
                    .define("enable_dt_textures", true);
            RENDER_ARMOUR = builder
                    .comment("When enabled, dogs with points in guard dog will have armour.")
                    .translation("doggytalents.config.client.render_armour")
                    .define("render_armour", false);
            RENDER_SADDLE = builder
                    .comment("When enabled, dogs with points in wolf mount will have a saddle on.")
                    .translation("doggytalents.config.client.render_saddle")
                    .define("render_saddle", true);
            RENDER_WINGS = builder
                    .comment("When enabled, Dogs will have wings when at level 5 pillow paw.")
                    .translation("doggytalents.config.client.render_wings")
                    .define("render_wings", false);
            RENDER_BLOOD = builder
                    .comment("When enabled, Dogs will show blood texture while incapacitated.")
                    .translation("doggytalents.config.client.render_incapacitated_overlay")
                    .define("render_incapacitated_overlay", true);

            builder.pop();
        }
    }

    public static class ServerConfig {

        public ForgeConfigSpec.BooleanValue DISABLE_HUNGER;
        public ForgeConfigSpec.BooleanValue STARTING_ITEMS;
        public ForgeConfigSpec.BooleanValue DOG_GENDER;
        public ForgeConfigSpec.BooleanValue PUPS_GET_PARENT_LEVELS;
        public ForgeConfigSpec.IntValue TIME_TO_MATURE;
        public ForgeConfigSpec.BooleanValue DOG_WHINE_WHEN_HUNGER_LOW;
        public ForgeConfigSpec.BooleanValue EAT_FOOD_ON_FLOOR;

        public Map<String, ForgeConfigSpec.BooleanValue> DISABLED_TALENTS;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            //DEBUG_MODE = builder
            //        .comment("Enables debugging mode, which would output values for the sake of finding issues in the mod.")
            //        .define("debugMode", false);

            builder.pop();
            builder.push("Dog Constants");

            DISABLE_HUNGER = builder
                    .comment("Disable hunger mode for the dog")
                    .translation("doggytalents.config.dog.disable_hunger")
                    .define("disable_hunger", false);
            STARTING_ITEMS = builder
                    .comment("When enabled you will spawn with a guide, Doggy Charm and Command Emblem.")
                    .translation("doggytalents.config.enable_starting_items")
                    .define("enable_starting_items", false);
            DOG_GENDER = builder
                    .comment("When enabled, dogs will be randomly assigned genders and will only mate and produce children with the opposite gender.")
                    .translation("doggytalents.config.enable_gender")
                    .define("enable_gender", true);
            PUPS_GET_PARENT_LEVELS = builder
                    .comment("When enabled, puppies get some levels from parents. When disabled, puppies start at 0 points.")
                    .translation("doggytalents.config.enable_pup_get_parent_levels")
                    .define("enable_pup_get_parent_levels", false);
            TIME_TO_MATURE = builder
                    .comment("The time in ticks it takes for a baby dog to become an adult, default 48000 (2 Minecraft days) and minimum 0")
                    .translation("doggytalents.config.dog.time_to_mature")
                    .defineInRange("time_to_mature", 48000, 0, Integer.MAX_VALUE);
            DOG_WHINE_WHEN_HUNGER_LOW = builder
                    .comment("Determines if dogs should whine when hunger reaches below 20 DP.")
                    .translation("doggytalents.config.whine_when_hungry")
                    .define("whine_when_hungry", true);
            EAT_FOOD_ON_FLOOR = builder
                    .comment("When enabled dogs will path and eat editable items in the world.")
                    .translation("doggytalents.config.eat_food_on_floor")
                    .define("eat_food_on_floor", true);

            builder.pop();
        }
    }

    public static class TalentConfig {
        public Map<IRegistryDelegate<Talent>, ForgeConfigSpec.BooleanValue> DISABLED_TALENTS;

        public TalentConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Here you can disable talents.").push("Talents");

            DISABLED_TALENTS = new HashMap<>();

            DoggyTalentsAPI.TALENTS.get().forEach((loc) ->
                DISABLED_TALENTS.put(loc.delegate, builder.define(loc.getRegistryName().toString(), true))
            );
            builder.pop();
        }

        public boolean getFlag(Talent talent) {
            ForgeConfigSpec.BooleanValue booleanValue = this.DISABLED_TALENTS.get(talent.delegate);
            if (booleanValue == null) {
                return true;
            }

            return booleanValue.get();
        }
    }
}
