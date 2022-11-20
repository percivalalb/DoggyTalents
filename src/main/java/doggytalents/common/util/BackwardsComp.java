package doggytalents.common.util;

import com.google.common.collect.Maps;
import doggytalents.*;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.entity.serializers.DimensionDependantArg;
import doggytalents.common.lib.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public static void readTalentMapping(CompoundTag compound, List<TalentInstance> talentMap) {
        if (compound.contains("talents", Tag.TAG_STRING)) {

            String[] split = compound.getString("talents").split(":");
            if (split.length > 0 && split.length % 2 == 0) {
                for (int i = 0; i < split.length; i += 2) {
                    final int levelIndex = i + 1;

                    BackwardsComp.getTalentMap(split[i]).ifPresent((talent) -> {
                        int level = 0;
                        try {
                            level = Integer.valueOf(split[levelIndex]);
                        } catch(Exception e) {
                            return;
                        }

                        if (talent != null) { // Only load if talent exists
                            talentMap.add(talent.get().getDefault(level));
                        }
                    });
                }
            }
        } else if (compound.contains("talent_level_list", Tag.TAG_LIST)) {

            ListTag list = compound.getList("talent_level_list", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag talentCompound = list.getCompound(i);
                Talent talent = NBTUtil.getRegistryValue(talentCompound, "talent", DoggyTalentsAPI.TALENTS.get());

                if (talent != null) { // Only load if talent exists
                    int level = talentCompound.getInt("level");
                    talentMap.add(talent.getDefault(level));
                }
            }
        }
    }

    public static void readAccessories(CompoundTag compound, List<AccessoryInstance> accessories) {
        if (compound.contains("radioCollar", Tag.TAG_BYTE) && compound.getBoolean("radioCollar")) {
            accessories.add(DoggyAccessories.RADIO_BAND.get().getDefault());
        }

        if (compound.contains("sunglasses", Tag.TAG_BYTE) && compound.getBoolean("sunglasses")) {
            accessories.add(DoggyAccessories.SUNGLASSES.get().getDefault());
        }

        if (compound.contains("capeData", Tag.TAG_ANY_NUMERIC)) {
            int value = compound.getInt("capeData");

            if (value >= 0) {
                accessories.add(DoggyAccessories.DYEABLE_CAPE.get().create(value));
            } else if (value >= -1) {
                accessories.add(DoggyAccessories.DYEABLE_CAPE.get().getDefault());
            } else if (value == -3) {
                accessories.add(DoggyAccessories.CAPE.get().getDefault());
            } else if (value == -4) {
                accessories.add(DoggyAccessories.LEATHER_JACKET_CLOTHING.get().getDefault());
            }

            // -2 indicated no cape, -5 a below were not used
        }

        if (compound.contains("collarColour", Tag.TAG_ANY_NUMERIC)) {
            int value = compound.getInt("collarColour");

            if (value >= 0) {
                accessories.add(DoggyAccessories.DYEABLE_COLLAR.get().create(value));
            } else if (value >= -1) {
                accessories.add(DoggyAccessories.DYEABLE_COLLAR.get().getDefault());
            } else if (value == -3) {
                accessories.add(DoggyAccessories.GOLDEN_COLLAR.get().getDefault());
            } else if (value == -4) {
                accessories.add(DoggyAccessories.SPOTTED_COLLAR.get().getDefault());
            } else if (value == -5) {
                accessories.add(DoggyAccessories.MULTICOLORED_COLLAR.get().getDefault());
            }

            // -2 indicated no cape, -6 a below were not used
        }
    }

    public static void readMode(CompoundTag compound, Consumer<EnumMode> consumer) {
        consumer.accept(EnumMode.byIndex(compound.getInt("mode")));
    }

    public static void readDogTexture(CompoundTag compound, Consumer<String> consumer) {
        if (compound.contains("doggyTex", Tag.TAG_ANY_NUMERIC)) {
            int value = compound.getInt("doggyTex");

            String[] textures = {
                "bad647ebc2ac822563eaedaa3cb64881d8d7fd24", "c22b2f6d7a902c13d2f9c377f360127b6ae2dd65", "bea8cace65c013ca9cdae76b0664f4176502e4fb",
                "df167655cf5db4147e28d6920862636ce94c22cd", "b82d3e99a8ef342fbdc5f5a3f6c91b3940f80f55", "84426b389b050105b2cc8883a28e21ded8502d15",
                "7281214e5b1c928e124de44af6040d34100ef11e", "7229d8e7b8e5a291d2d8edb7082e43974bc409f9", "40cde1ba3ab392860432d00042dde4ea5ae2accd",
                "7a50feaf4e5d9a332946afa66731430282adaf06", "9846a3d8b29589e2ed125b5ce8b8fe398ee4b389", "62b05471528c8268c5de6ec514ba801eea812e99",
                "9a6c7833aebe88d09e2c4807cf9ab7b2107ce23b", "b4b0b25d37c7790b886e5feafcc794e13d801d2f", "08bf1e51a9a64e1b82518360c05fddd711ba0071",
            };

            if (value >= 0 && value < textures.length) {
                consumer.accept(textures[value]);
            }
        }
    }

    public static void readHasBone(CompoundTag compound, Consumer<ItemStack> consumer) {
        if (compound.contains("hasBone", Tag.TAG_BYTE) && compound.getBoolean("hasBone")) {
            int variant = compound.getInt("boneVariant");
            if (variant == 0) {
                consumer.accept(new ItemStack(DoggyItems.THROW_BONE.get()));
            } else if (variant == 1) {
                consumer.accept(new ItemStack(DoggyItems.THROW_STICK.get()));
            }
        }
    }

    public static void readBowlLocations(CompoundTag compound, DimensionDependantArg<Optional<BlockPos>> bowlsData) {
        if (compound.contains("bowlPosX", Tag.TAG_ANY_NUMERIC)) {
            bowlsData.put(Level.OVERWORLD, Optional.of(new BlockPos(compound.getInt("bowlPosX"), compound.getInt("bowlPosY"), compound.getInt("bowlPosZ"))));
        }
    }

    public static void readBedLocations(CompoundTag compound, DimensionDependantArg<Optional<BlockPos>> bedsData) {
        if (compound.contains("bedPosX", Tag.TAG_ANY_NUMERIC)) {
            bedsData.put(Level.OVERWORLD, Optional.of(new BlockPos(compound.getInt("bedPosX"), compound.getInt("bedPosY"), compound.getInt("bedPosZ"))));
        }
    }

    public static void remapMissingEntities(final MissingMappingsEvent event) {
        if (ForgeRegistries.Keys.ENTITY_TYPES.equals(event.getRegistry().getRegistryName())) {

            List<MissingMappingsEvent.Mapping<EntityType<?>>> mappings = event.getMappings(ForgeRegistries.Keys.ENTITY_TYPES, Constants.MOD_ID);

            if (mappings == null) {
                DoggyTalents2.LOGGER.warn("Failed to attempt to remap missing mapppings.");
                return;
            }

            mappings.forEach(mapping -> {
                if (Objects.equals(mapping.getKey(), BackwardsComp.DOGGY_BEAM)) {
                    mapping.remap(DoggyEntityTypes.DOG_BEAM.get());
                    DoggyTalents2.LOGGER.debug("Remapped Dog Beam id");
                }
            });
        } else if (ForgeRegistries.Keys.ITEMS.equals(event.getRegistry().getRegistryName())) {
            List<MissingMappingsEvent.Mapping<Item>> mappings = event.getMappings(ForgeRegistries.Keys.ITEMS, Constants.MOD_ID);
            if (mappings == null) {
                DoggyTalents2.LOGGER.warn("Failed to attempt to remap missing mapppings.");
                return;
            }

            mappings.forEach(mapping -> {
                if (Objects.equals(mapping.getKey(), BackwardsComp.COMMAND_EMBLEM)) {
                    mapping.remap(DoggyItems.WHISTLE.get());
                    DoggyTalents2.LOGGER.debug("Remapped Command Emblem to Whistle");
                }
            });
        }
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

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(BackwardsComp::remapMissingEntities);
    }
}
