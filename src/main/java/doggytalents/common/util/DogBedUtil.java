package doggytalents.common.util;

import doggytalents.DoggyBlocks;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.block.tileentity.DogBedTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DogBedUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void setBedVariant(DogBedTileEntity dogBedTileEntity, ItemStack stack) {
        Pair<ICasingMaterial, IBeddingMaterial> materials = DogBedUtil.getMaterials(stack);

        dogBedTileEntity.setCasing(materials.getLeft());
        dogBedTileEntity.setBedding(materials.getRight());
    }

    public static ItemStack createRandomBed() {
        ICasingMaterial casing = pickRandom(DoggyTalentsAPI.CASING_MATERIAL.get());
        IBeddingMaterial bedding = pickRandom(DoggyTalentsAPI.BEDDING_MATERIAL.get());
        return DogBedUtil.createItemStack(casing, bedding);
    }

    public static Pair<ICasingMaterial, IBeddingMaterial> getMaterials(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("doggytalents");
        if (tag != null) {
            return Pair.of(
                NBTUtil.getRegistryValue(tag, "casingId", DoggyTalentsAPI.CASING_MATERIAL.get()),
                NBTUtil.getRegistryValue(tag, "beddingId", DoggyTalentsAPI.BEDDING_MATERIAL.get())
            );
        }

        return Pair.of(null, null);
    }

    public static ItemStack createItemStack(ICasingMaterial casingId, IBeddingMaterial beddingId) {
        ItemStack stack = new ItemStack(DoggyBlocks.DOG_BED.get(), 1);

        CompoundTag tag = stack.getOrCreateTagElement("doggytalents");
        NBTUtil.putRegistryValue(tag, "casingId", casingId, DoggyTalentsAPI.CASING_MATERIAL.get());
        NBTUtil.putRegistryValue(tag, "beddingId", beddingId, DoggyTalentsAPI.BEDDING_MATERIAL.get());

        return stack;
    }

    public static ICasingMaterial getCasingFromStack(IForgeRegistry<ICasingMaterial> registry, ItemStack stack) {
        for (ICasingMaterial m : registry.getValues()) {
            if (m.getIngredient().test(stack)) {
                return m;
            }
        }

        return null;
    }

    public static IBeddingMaterial getBeddingFromStack(IForgeRegistry<IBeddingMaterial> registry, ItemStack stack) {
        for (IBeddingMaterial m : registry.getValues()) {
            if (m.getIngredient().test(stack)) {
                return m;
            }
        }

        return null;
    }

    public static <T> T pickRandom(IForgeRegistry<T> registry) {
        Collection<T> values = registry.getValues();
        List<T> list = values instanceof List ? (List<T>) values : new ArrayList<>(values);
        return list.get(RANDOM.nextInt(list.size()));
    }
}
