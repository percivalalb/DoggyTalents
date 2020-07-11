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

    public static void putBeddingMapping(String current, String... previous) {
        for (String old : previous) {
            OLD_NEW_BED.put(old, current);
        }
    }

    public static void init() {
        // Update to 1.13 and then 1.14
        putBeddingMapping("minecraft:white_wool", "minecraft:wool.0", "minecraft_white_wool");
        putBeddingMapping("minecraft:orange_wool", "minecraft:wool.1", "minecraft_orange_wool");
        putBeddingMapping("minecraft:magenta_wool", "minecraft:wool.2", "minecraft_magenta_wool");
        putBeddingMapping("minecraft:light_blue_wool", "minecraft:wool.3", "minecraft_light_blue_wool");
        putBeddingMapping("minecraft:yellow_wool", "minecraft:wool.4", "minecraft_yellow_wool");
        putBeddingMapping("minecraft:lime_wool", "minecraft:wool.5", "minecraft_lime_wool");
        putBeddingMapping("minecraft:pink_wool", "minecraft:wool.6", "minecraft_pink_wool");
        putBeddingMapping("minecraft:gray_wool", "minecraft:wool.7", "minecraft_gray_wool");
        putBeddingMapping("minecraft:light_gray_wool", "minecraft:wool.8", "minecraft_light_gray_wool");
        putBeddingMapping("minecraft:cyan_wool", "minecraft:wool.9", "minecraft_cyan_wool");
        putBeddingMapping("minecraft:purple_wool", "minecraft:wool.10", "minecraft_purple_wool");
        putBeddingMapping("minecraft:blue_wool", "minecraft:wool.11", "minecraft_blue_wool");
        putBeddingMapping("minecraft:brown_wool", "minecraft:wool.12", "minecraft_brown_wool");
        putBeddingMapping("minecraft:green_wool", "minecraft:wool.13", "minecraft_green_wool");
        putBeddingMapping("minecraft:red_wool", "minecraft:wool.14", "minecraft_red_wool");
        putBeddingMapping("minecraft:black_wool", "minecraft:wool.15", "minecraft_black_wool");
        putBeddingMapping("minecraft:oak_planks", "minecraft:planks.0", "minecraft_oak_planks");
        putBeddingMapping("minecraft:spruce_planks", "minecraft:planks.1", "minecraft_spruce_planks");
        putBeddingMapping("minecraft:birch_planks", "minecraft:planks.2", "minecraft_birch_planks");
        putBeddingMapping("minecraft:jungle_planks", "minecraft:planks.3", "minecraft_jungle_planks");
        putBeddingMapping("minecraft:acacia_planks", "minecraft:planks.4", "minecraft_acacia_planks");
        putBeddingMapping("minecraft:dark_oak_planks", "minecraft:planks.5", "minecraft_dark_oak_planks");

        //TODO
        OLD_NEW_TALENT.put("bedfinder", DoggyTalents.BED_FINDER);
        OLD_NEW_TALENT.put("blackpelt", DoggyTalents.BLACK_PELT);
        OLD_NEW_TALENT.put("creepersweeper", DoggyTalents.CREEPER_SWEEPER);
        OLD_NEW_TALENT.put("doggydash", DoggyTalents.DOGGY_DASH);
        OLD_NEW_TALENT.put("fisherdog", DoggyTalents.FISHER_DOG);
        OLD_NEW_TALENT.put("guarddog", DoggyTalents.GUARD_DOG);
        OLD_NEW_TALENT.put("happyeater", DoggyTalents.HAPPY_EATER);
        OLD_NEW_TALENT.put("hellhound", DoggyTalents.HELL_HOUND);
        OLD_NEW_TALENT.put("hunterdog", DoggyTalents.HUNTER_DOG);
        OLD_NEW_TALENT.put("packpuppy", DoggyTalents.PACK_PUPPY);
        OLD_NEW_TALENT.put("pestfighter", DoggyTalents.PEST_FIGHTER);
        OLD_NEW_TALENT.put("pillowpaw", DoggyTalents.PILLOW_PAW);
        OLD_NEW_TALENT.put("poisonfang", DoggyTalents.POISON_FANG);
        OLD_NEW_TALENT.put("puppyeyes", DoggyTalents.PUPPY_EYES);
        OLD_NEW_TALENT.put("quickhealer", DoggyTalents.QUICK_HEALER);
        //OLD_NEW_TALENT.put("rangedattacker", DoggyTalents.RANGED_ATTACKER);
        OLD_NEW_TALENT.put("rescuedog", DoggyTalents.RESCUE_DOG);
        OLD_NEW_TALENT.put("roaringgale", DoggyTalents.ROARING_GALE);
        OLD_NEW_TALENT.put("shepherddog", DoggyTalents.SHEPHERD_DOG);
        OLD_NEW_TALENT.put("swimmerdog", DoggyTalents.SWIMMER_DOG);
        OLD_NEW_TALENT.put("wolfmount", DoggyTalents.WOLF_MOUNT);
    }
}
