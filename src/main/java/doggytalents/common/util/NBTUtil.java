package doggytalents.common.util;

import doggytalents.DoggyTalents2;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class NBTUtil {

    /**
     * Writes the UUID to the CompoundNBT under the given key if it is not null
     */
    public static void putUniqueId(CompoundTag compound, String key, @Nullable UUID uuid) {
        if (uuid != null) {
            compound.putUUID(key, uuid);
        }
    }

    /**
     * Reads the UUID from the CompoundNBT if it exists returns null otherwise
     */
    @Nullable
    public static UUID getUniqueId(CompoundTag compound, String key) {
        if (compound.hasUUID(key)) {
            return compound.getUUID(key);
        } else if (NBTUtil.hasOldUniqueId(compound, key)) {
            return NBTUtil.getOldUniqueId(compound, key);
        }

        return null;
    }

    public static UUID getOldUniqueId(CompoundTag compound, String key) {
        return new UUID(compound.getLong(key + "Most"), compound.getLong(key + "Least"));
    }

    public static boolean hasOldUniqueId(CompoundTag compound, String key) {
        return compound.contains(key + "Most", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains(key + "Least", Constants.NBT.TAG_ANY_NUMERIC);
    }

    public static void removeOldUniqueId(CompoundTag compound, String key) {
        compound.remove(key + "Most");
        compound.remove(key + "Least");
    }

    public static void putResourceLocation(CompoundTag compound, String key, @Nullable ResourceLocation rl) {
        if (rl != null) {
            compound.putString(key, rl.toString());
        }
    }

    @Nullable
    public static ResourceLocation getResourceLocation(CompoundTag compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_STRING)) {
            return ResourceLocation.tryParse(compound.getString(key));
        }

        return null;
    }

    @Nullable
    public static void putVector3d(CompoundTag compound, @Nullable Vec3 vec3d) {
        if (vec3d != null) {
            compound.putDouble("x", vec3d.x());
            compound.putDouble("y", vec3d.y());
            compound.putDouble("z", vec3d.z());
        }
    }

    @Nullable
    public static Vec3 getVector3d(CompoundTag compound) {
        if (compound.contains("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new Vec3(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
        }

        return null;
    }


    public static void putTextComponent(CompoundTag compound, String key, @Nullable Component component) {
        if (component != null) {
            compound.putString(key, Component.Serializer.toJson(component));
        }
    }

    @Nullable
    public static Component getTextComponent(CompoundTag compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_STRING)) {
            return Component.Serializer.fromJson(compound.getString(key));
        }

        return null;
    }

    @Nullable
    public static <T extends IForgeRegistryEntry<T>> T getRegistryValue(CompoundTag compound, String key, IForgeRegistry<T> registry) {
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

    public static <T extends IForgeRegistryEntry<T>> void putRegistryValue(CompoundTag compound, String key, T value) {
        if (value != null) {
            NBTUtil.putResourceLocation(compound, key, value.getRegistryName());
        }
    }

    public static void putBlockPos(CompoundTag compound, @Nullable BlockPos vec3d) {
        if (vec3d != null) {
            compound.putInt("x", vec3d.getX());
            compound.putInt("y", vec3d.getY());
            compound.putInt("z", vec3d.getZ());
        }
    }

    @Nullable
    public static BlockPos getBlockPos(CompoundTag compound) {
        if (compound.contains("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.contains("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
        }

        return null;
    }


    public static void putBlockPos(CompoundTag compound, String key, Optional<BlockPos> vec3d) {
        if (vec3d.isPresent()) {
            CompoundTag posNBT = new CompoundTag();
            putBlockPos(posNBT, vec3d.get());
            compound.put(key, posNBT);
        }
    }

    public static Optional<BlockPos> getBlockPos(CompoundTag compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return Optional.of(getBlockPos(compound.getCompound(key)));
        }

        return Optional.empty();
    }

    public static void putBlockPos(CompoundTag compound, String key, @Nullable BlockPos vec3d) {
        if (vec3d != null) {
            CompoundTag posNBT = new CompoundTag();
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

    public static void writeItemStack(CompoundTag compound, String key, ItemStack stackIn) {
        if (!stackIn.isEmpty()) {
            compound.put(key, stackIn.save(new CompoundTag()));
        }
    }

    @Nonnull
    public static ItemStack readItemStack(CompoundTag compound, String key) {
        if (compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return ItemStack.of(compound.getCompound(key));
        }

        return ItemStack.EMPTY;
    }
}
