package doggytalents.common.util;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.DoggyBlocks;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.common.block.DogBedRegistry;
import doggytalents.common.block.tileentity.DogBedTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DogBedUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void setBedVariant(DogBedTileEntity dogBedTileEntity, ItemStack stack) {
        Pair<IBedMaterial, IBedMaterial> materials = DogBedUtil.getMaterials(stack);

        dogBedTileEntity.setCasing(materials.getLeft());
        dogBedTileEntity.setBedding(materials.getRight());
    }

    public static ItemStack createRandomBed() {
        IBedMaterial casing = pickRandom(DogBedRegistry.CASINGS.getKeys());
        IBedMaterial bedding = pickRandom(DogBedRegistry.BEDDINGS.getKeys());
        return DogBedUtil.createItemStack(casing, bedding);
    }

    public static Pair<IBedMaterial, IBedMaterial> getMaterials(ItemStack stack) {
        CompoundNBT tag = stack.getChildTag("doggytalents");
        if (tag != null) {
            IBedMaterial casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId"));
            IBedMaterial beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId"));

            return Pair.of(casingId, beddingId);
        }

        return Pair.of(IBedMaterial.NULL, IBedMaterial.NULL);
    }

    public static ItemStack createItemStack(IBedMaterial casingId, IBedMaterial beddingId) {
        ItemStack stack = new ItemStack(DoggyBlocks.DOG_BED.get(), 1);

        CompoundNBT tag = stack.getOrCreateChildTag("doggytalents");
        tag.putString("casingId", casingId.getSaveId());
        tag.putString("beddingId", beddingId.getSaveId());
        return stack;
    }

    public static IBedMaterial pickRandom(List<IBedMaterial> strs) {
        return strs.get(RANDOM.nextInt(strs.size()));
    }
}
