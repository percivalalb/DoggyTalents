package doggytalents.common.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class NBTUtil {

    /**
     * Writes the UUID to the CompoundNBT under the given key if it is not null
     */
    public static void putUniqueId(CompoundNBT compound, String key, @Nullable UUID uuid) {
        if (uuid != null) {
            compound.putUniqueId(key, uuid);
        }
    }

    /**
     * Reads the UUID from the CompoundNBT if it exists returns null otherwise
     */
    @Nullable
    public static UUID getUniqueId(CompoundNBT compound, String key) {
        if (compound.hasUniqueId(key)) {
            return compound.getUniqueId(key);
        }

        return null;
    }

    public static void putResourceLocation(CompoundNBT compound, String key, @Nullable ResourceLocation rl) {
        if (rl != null) {
            compound.putString(key, rl.toString());
        }
    }

    @Nullable
    public static ResourceLocation getResourceLocation(CompoundNBT compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_STRING)) {
            return ResourceLocation.tryCreate(compound.getString(key));
        }

        return null;
    }

    @Nullable
    public static void putVector3d(CompoundNBT compound, @Nullable Vector3d vec3d) {
        if (vec3d != null) {
            compound.putDouble("x", vec3d.getX());
            compound.putDouble("y", vec3d.getY());
            compound.putDouble("z", vec3d.getZ());
        }
    }

    @Nullable
    public static Vector3d getVector3d(CompoundNBT compound) {
        if (compound.contains("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new Vector3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
        }

        return null;
    }


    public static void putTextComponent(CompoundNBT compound, String key, @Nullable ITextComponent component) {
        if (component != null) {
            compound.putString(key, ITextComponent.Serializer.toJson(component));
        }
    }

    @Nullable
    public static ITextComponent getTextComponent(CompoundNBT compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_STRING)) {
            return ITextComponent.Serializer.getComponentFromJson(compound.getString(key));
        }

        return null;
    }

    public static Map<Talent, Integer> getTalentMap(CompoundNBT compound, String key) {
        Map<Talent, Integer> talentMap = Maps.newHashMap();

        ListNBT list = compound.getList(key, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT talentCompound = list.getCompound(i);
            Talent talent = NBTUtil.getRegistryValue(talentCompound, "talent", DoggyTalentsAPI.TALENTS);

            if (talent != null) { // Only load if talent exists
                int level = talentCompound.getInt("level");
                talentMap.put(talent, level);
            }
        }

        return talentMap;
    }


    public static void putTalentMap(CompoundNBT compound, String key, Map<Talent, Integer> talentMap) {
        if (!talentMap.isEmpty()) {
            ListNBT list = new ListNBT();

            for (Entry<Talent, Integer> entry : talentMap.entrySet()) {
                CompoundNBT talentCompound = new CompoundNBT();

                NBTUtil.putRegistryValue(talentCompound, "talent", entry.getKey());
                talentCompound.putInt("level", entry.getValue());

                list.add(talentCompound);
            }

            compound.put(key, list);
        }
    }

    @Nullable
    public static <T extends IForgeRegistryEntry<T>> T getRegistryValue(CompoundNBT compound, String key, IForgeRegistry<T> registry) {
        ResourceLocation rl = NBTUtil.getResourceLocation(compound, key);
        if (rl != null) {
            if (registry.containsKey(rl)) {
                return registry.getValue(rl);
            } else {
                DoggyTalents2.LOGGER.warn("Unable to load registry value in registry {} with resource location {}", registry.getRegistryName(), rl);
            }
        } else {
            DoggyTalents2.LOGGER.warn("Unable to load resource location in NBT:{}, for {} registry", key, registry.getRegistryName());
        }

        return null;
    }

    public static <T extends IForgeRegistryEntry<T>> void putRegistryValue(CompoundNBT compound, String key, T value) {
        if (value != null) {
            NBTUtil.putResourceLocation(compound, key, value.getRegistryName());
        }
    }

    public static void putBlockPos(CompoundNBT compound, @Nullable BlockPos vec3d) {
        if (vec3d != null) {
            compound.putInt("x", vec3d.getX());
            compound.putInt("y", vec3d.getY());
            compound.putInt("z", vec3d.getZ());
        }
    }

    @Nullable
    public static BlockPos getBlockPos(CompoundNBT compound) {
        if (compound.contains("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
        }

        return null;
    }


    public static void putBlockPos(CompoundNBT compound, String key, Optional<BlockPos> vec3d) {
        if (vec3d.isPresent()) {
            CompoundNBT posNBT = new CompoundNBT();
            putBlockPos(posNBT, vec3d.get());
            compound.put(key, posNBT);
        }
    }

    public static Optional<BlockPos> getBlockPos(CompoundNBT compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return Optional.of(getBlockPos(compound.getCompound(key)));
        }

        return Optional.empty();
    }

    public static void putBlockPos(CompoundNBT compound, String key, @Nullable BlockPos vec3d) {
        if (vec3d != null) {
            CompoundNBT posNBT = new CompoundNBT();
            putBlockPos(posNBT, vec3d);
            compound.put(key, posNBT);
        }
    }

//    @Nullable
//    public static BlockPos getBlockPos(CompoundNBT compound, String key) {
//        if (compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
//            return getBlockPos(compound.getCompound(key));
//        }
//
//        return null;
//    }

    public static void writeItemStack(CompoundNBT compound, String key, ItemStack stackIn) {
        if (!stackIn.isEmpty()) {
            compound.put(key, stackIn.write(new CompoundNBT()));
        }
    }

    @Nonnull
    public static ItemStack readItemStack(CompoundNBT compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return ItemStack.read(compound.getCompound(key));
        }

        return ItemStack.EMPTY;
    }
}
