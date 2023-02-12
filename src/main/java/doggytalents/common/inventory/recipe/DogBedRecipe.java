package doggytalents.common.inventory.recipe;

import doggytalents.DoggyRecipeSerializers;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.util.DogBedUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class DogBedRecipe extends CustomRecipe implements IShapedRecipe<CraftingContainer> {

    public DogBedRecipe(ResourceLocation resource, CraftingBookCategory craftingBookCategory) {
        super(resource, craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        IBeddingMaterial beddingId = null;
        ICasingMaterial casingId = null;

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (col == 1 && row < 2) {
                    IBeddingMaterial id = DogBedUtil.getBeddingFromStack(DoggyTalentsAPI.BEDDING_MATERIAL.get(), inv.getItem(row * inv.getWidth() + col));

                    if (id == null) {
                        return false;
                    }

                    if (beddingId == null) {
                        beddingId = id;
                    } else if (beddingId != id) {
                        return false;
                    }
                }
                else {
                    ICasingMaterial id = DogBedUtil.getCasingFromStack(DoggyTalentsAPI.CASING_MATERIAL.get(), inv.getItem(row * inv.getWidth() + col));

                    if (id == null) {
                        return false;
                    }

                    if (casingId == null) {
                        casingId = id;
                    } else if (casingId != id) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        IBeddingMaterial beddingId = DogBedUtil.getBeddingFromStack(DoggyTalentsAPI.BEDDING_MATERIAL.get(), inv.getItem(1));
        ICasingMaterial casingId = DogBedUtil.getCasingFromStack(DoggyTalentsAPI.CASING_MATERIAL.get(), inv.getItem(0));

        return DogBedUtil.createItemStack(casingId, beddingId);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(itemstack));
        }

        return nonnulllist;
    }

    //Is on a 3x3 grid or bigger
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DoggyRecipeSerializers.DOG_BED.get();
    }

    @Override
    public int getRecipeWidth() {
        return 3;
    }

    @Override
    public int getRecipeHeight() {
        return 3;
    }
}
