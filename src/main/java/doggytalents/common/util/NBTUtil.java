package doggytalents.common.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
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
    public static void putVec3d(CompoundNBT compound, @Nullable Vec3d vec3d) {
        if (vec3d != null) {
            compound.putDouble("x", vec3d.getX());
            compound.putDouble("y", vec3d.getY());
            compound.putDouble("z", vec3d.getZ());
        }
    }

    @Nullable
    public static Vec3d getVec3d(CompoundNBT compound) {
        if (compound.contains("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new Vec3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
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
            return ITextComponent.Serializer.fromJson(compound.getString(key));
        }

        return null;
    }

    public static Map<Talent, Integer> getTalentMap(CompoundNBT compound, String key) {
        Map<Talent, Integer> talentMap = Maps.newHashMap();

        ListNBT list = compound.getList(key, Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.size(); i++) {
            CompoundNBT talentCompound = list.getCompound(i);
            ResourceLocation talentId = NBTUtil.getResourceLocation(talentCompound, "talent");

            if(DoggyTalentsAPI.TALENTS.containsKey(talentId)) { // Only load if talent exists
                int level = talentCompound.getInt("level");
                talentMap.put(DoggyTalentsAPI.TALENTS.getValue(talentId), level);
            }
        }

        return talentMap;
    }


    public static void putTalentMap(CompoundNBT compound, String key, Map<Talent, Integer> talentMap) {
        if (!talentMap.isEmpty()) {
            ListNBT list = new ListNBT();

            for(Entry<Talent, Integer> entry : talentMap.entrySet()) {
                CompoundNBT talentCompound = new CompoundNBT();

                NBTUtil.putRegistryValue(talentCompound, "talent", entry.getKey());
                talentCompound.putInt("level", entry.getValue());

                list.add(talentCompound);
            }

            compound.put(key, list);
        }
    }

    public static <T extends IForgeRegistryEntry<T>> T getRegistryValue(CompoundNBT compound, String key, IForgeRegistry<T> registry) {
        //TODO Checks
        return registry.getValue(NBTUtil.getResourceLocation(compound, key));
    }

    public static <T extends IForgeRegistryEntry<T>> void putRegistryValue(CompoundNBT compound, String key, T value) {
        //TODO Checks
        if (value != null) {
            DoggyTalents2.LOGGER.info("{}", value.getRegistryName());
            NBTUtil.putResourceLocation(compound, key, value.getRegistryName());
        }
    }
}
