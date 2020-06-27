package doggytalents.common.util;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.DoggyTalents;
import doggytalents.DoggyTalents2;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BackwardsComp {

    public static final ResourceLocation DOGGY_BEAM = Util.getResource("attackbeam");
    public static final ResourceLocation COMMAND_EMBLEM = Util.getResource("command_emblem");

    private static Map<String, String> OLD_NEW_BED = Maps.newHashMap();
    private static Map<String, Supplier<? extends Talent>> OLD_NEW_TALENT = Maps.newHashMap();

    public static Optional<String> getBedMaterialMapping(String oldId) {
        return Optional.ofNullable(OLD_NEW_BED.get(oldId));
    }

//    private static Map<String, Supplier<? extends IBedMaterial>> OLD_NEW_BED = Maps.newHashMap();
//
//    public static Optional<Supplier<? extends IBedMaterial>> getBedMaterialMapping(String oldId) {
//        return Optional.ofNullable(OLD_NEW_BED.get(oldId));
//    }

    public static Optional<Supplier<? extends Talent>> getTalentMap(String oldId) {
        return Optional.ofNullable(OLD_NEW_TALENT.get(oldId));
    }

    public static void readTalentMapping(CompoundNBT compound, Map<Talent, Integer> talentMap) {
        if(compound.contains("talents", Constants.NBT.TAG_STRING)) {

            String[] split = compound.getString("talents").split(":");
            if(split.length > 0 && split.length % 2 == 0) {
                for(int i = 0; i < split.length; i += 2) {
                    final int levelIndex = i + 1;

                    BackwardsComp.getTalentMap(split[i]).ifPresent((talent) -> {
                        int level = 0;
                        try {
                            level = Integer.valueOf(split[levelIndex]);
                        } catch(Exception e) {
                            return;
                        }

                        if(talent != null) { // Only load if talent exists
                            talentMap.put(talent.get(), level);
                        }
                    });
                }
            }
        }
    }

    public static void readMode(CompoundNBT compound, Consumer<EnumMode> consumer) {
        consumer.accept(EnumMode.byIndex(compound.getInt("mode")));
    }

    @SubscribeEvent
    public void remapMissingEntities(final RegistryEvent.MissingMappings<EntityType<?>> event) {
        ImmutableList<Mapping<EntityType<?>>> mappings = event.getAllMappings();

        if (mappings == null) {
            DoggyTalents2.LOGGER.warn("Failed to attempt to remap missing mapppings.");
            return;
        }

        mappings.forEach(mapping -> {
            if(Objects.equals(mapping.key, BackwardsComp.DOGGY_BEAM)) {
                mapping.remap(DoggyEntityTypes.DOG_BEAM.get());
                DoggyTalents2.LOGGER.debug("Remapped Dog Beam id");
            }
        });
    }

    @SubscribeEvent
    public void remapMissingItems(final RegistryEvent.MissingMappings<Item> event) {
        ImmutableList<Mapping<Item>> mappings = event.getAllMappings();
        if (mappings == null) {
            DoggyTalents2.LOGGER.warn("Failed to attempt to remap missing mapppings.");
            return;
        }

        mappings.forEach(mapping -> {
            if(Objects.equals(mapping.key, BackwardsComp.COMMAND_EMBLEM)) {
                mapping.remap(DoggyItems.WHISTLE.get());
                DoggyTalents2.LOGGER.debug("Remapped Command Emblem to Whistle");
            }
        });
    }

    public static void init() {
        // Update to 1.13
        OLD_NEW_BED.put("minecraft:wool.0", "minecraft:white_wool");
        OLD_NEW_BED.put("minecraft:wool.1", "minecraft:orange_wool");
        OLD_NEW_BED.put("minecraft:wool.2", "minecraft:magenta_wool");
        OLD_NEW_BED.put("minecraft:wool.3", "minecraft:light_blue_wool");
        OLD_NEW_BED.put("minecraft:wool.4", "minecraft:yellow_wool");
        OLD_NEW_BED.put("minecraft:wool.5", "minecraft:lime_wool");
        OLD_NEW_BED.put("minecraft:wool.6", "minecraft:pink_wool");
        OLD_NEW_BED.put("minecraft:wool.7", "minecraft:gray_wool");
        OLD_NEW_BED.put("minecraft:wool.8", "minecraft:light_gray_wool");
        OLD_NEW_BED.put("minecraft:wool.9", "minecraft:cyan_wool");
        OLD_NEW_BED.put("minecraft:wool.10", "minecraft:purple_wool");
        OLD_NEW_BED.put("minecraft:wool.11", "minecraft:blue_wool");
        OLD_NEW_BED.put("minecraft:wool.12", "minecraft:brown_wool");
        OLD_NEW_BED.put("minecraft:wool.13", "minecraft:green_wool");
        OLD_NEW_BED.put("minecraft:wool.14", "minecraft:red_wool");
        OLD_NEW_BED.put("minecraft:wool.15", "minecraft:black_wool");
        OLD_NEW_BED.put("minecraft:planks.0", "minecraft:oak_planks");
        OLD_NEW_BED.put("minecraft:planks.1", "minecraft:spruce_planks");
        OLD_NEW_BED.put("minecraft:planks.2", "minecraft:birch_planks");
        OLD_NEW_BED.put("minecraft:planks.3", "minecraft:jungle_planks");
        OLD_NEW_BED.put("minecraft:planks.4", "minecraft:acacia_planks");
        OLD_NEW_BED.put("minecraft:planks.5", "minecraft:dark_oak_planks");

        // Update to 1.14
        OLD_NEW_BED.put("minecraft_white_wool", "minecraft:white_wool");
        OLD_NEW_BED.put("minecraft_orange_wool", "minecraft:orange_wool");
        OLD_NEW_BED.put("minecraft_magenta_wool", "minecraft:magenta_wool");
        OLD_NEW_BED.put("minecraft_light_blue_wool", "minecraft:light_blue_wool");
        OLD_NEW_BED.put("minecraft_yellow_wool", "minecraft:yellow_wool");
        OLD_NEW_BED.put("minecraft_lime_wool", "minecraft:lime_wool");
        OLD_NEW_BED.put("minecraft_pink_wool", "minecraft:pink_wool");
        OLD_NEW_BED.put("minecraft_gray_wool", "minecraft:gray_wool");
        OLD_NEW_BED.put("minecraft_light_gray_wool", "minecraft:light_gray_wool");
        OLD_NEW_BED.put("minecraft_cyan_wool", "minecraft:cyan_wool");
        OLD_NEW_BED.put("minecraft_purple_wool", "minecraft:purple_wool");
        OLD_NEW_BED.put("minecraft_blue_wool", "minecraft:blue_wool");
        OLD_NEW_BED.put("minecraft_brown_wool", "minecraft:brown_wool");
        OLD_NEW_BED.put("minecraft_green_wool", "minecraft:green_wool");
        OLD_NEW_BED.put("minecraft_red_wool", "minecraft:red_wool");
        OLD_NEW_BED.put("minecraft_black_wool", "minecraft:black_wool");
        OLD_NEW_BED.put("minecraft_oak_planks", "minecraft:oak_planks");
        OLD_NEW_BED.put("minecraft_spruce_planks", "minecraft:spruce_planks");
        OLD_NEW_BED.put("minecraft_birch_planks", "minecraft:birch_planks");
        OLD_NEW_BED.put("minecraft_jungle_planks", "minecraft:jungle_planks");
        OLD_NEW_BED.put("minecraft_acacia_planks", "minecraft:acacia_planks");
        OLD_NEW_BED.put("minecraft_dark_oak_planks", "minecraft:dark_oak_planks");

        //TODO
        OLD_NEW_TALENT.put("bedfinder", DoggyTalents.BED_FINDER.delegate);
        OLD_NEW_TALENT.put("blackpelt", DoggyTalents.BLACK_PELT.delegate);
        OLD_NEW_TALENT.put("creepersweeper", DoggyTalents.CREEPER_SWEEPER.delegate);
        OLD_NEW_TALENT.put("doggydash", DoggyTalents.DOGGY_DASH.delegate);
        OLD_NEW_TALENT.put("fisherdog", DoggyTalents.FISHER_DOG.delegate);
        OLD_NEW_TALENT.put("guarddog", DoggyTalents.GUARD_DOG.delegate);
        OLD_NEW_TALENT.put("happyeater", DoggyTalents.HAPPY_EATER.delegate);
        OLD_NEW_TALENT.put("hellhound", DoggyTalents.HELL_HOUND.delegate);
        OLD_NEW_TALENT.put("hunterdog", DoggyTalents.HUNTER_DOG.delegate);
        OLD_NEW_TALENT.put("packpuppy", DoggyTalents.PACK_PUPPY.delegate);
        OLD_NEW_TALENT.put("pestfighter", DoggyTalents.PEST_FIGHTER.delegate);
        OLD_NEW_TALENT.put("pillowpaw", DoggyTalents.PILLOW_PAW.delegate);
        OLD_NEW_TALENT.put("poisonfang", DoggyTalents.POISON_FANG.delegate);
        OLD_NEW_TALENT.put("puppyeyes", DoggyTalents.PUPPY_EYES.delegate);
        OLD_NEW_TALENT.put("quickhealer", DoggyTalents.QUICK_HEALER.delegate);
        //OLD_NEW_TALENT.put("rangedattacker", DoggyTalents.RANGED_ATTACKER.delegate);
        OLD_NEW_TALENT.put("rescuedog", DoggyTalents.RESCUE_DOG.delegate);
        OLD_NEW_TALENT.put("roaringgale", DoggyTalents.ROARING_GALE.delegate);
        OLD_NEW_TALENT.put("shepherddog", DoggyTalents.SHEPHERD_DOG.delegate);
        OLD_NEW_TALENT.put("swimmerdog", DoggyTalents.SWIMMER_DOG.delegate);
        OLD_NEW_TALENT.put("wolfmount", DoggyTalents.WOLF_MOUNT.delegate);
    }
}
