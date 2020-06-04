package doggytalents.common.inventory.recipe;

import doggytalents.DoggyRecipeSerializers;
import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.BeddingMaterial;
import doggytalents.api.registry.CasingMaterial;
import doggytalents.common.util.DogBedUtil;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class DogBedRecipe extends SpecialRecipe implements IShapedRecipe<CraftingInventory> {

    public DogBedRecipe(ResourceLocation resource) {
        super(resource);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        BeddingMaterial beddingId = null;
        CasingMaterial casingId = null;

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (col == 1 && row < 2) {
                    BeddingMaterial id = DogBedUtil.getBeddingFromStack(DoggyTalentsAPI.BEDDING_MATERIAL, inv.getStackInSlot(row * inv.getWidth() + col));

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
                    CasingMaterial id = DogBedUtil.getCasingFromStack(DoggyTalentsAPI.CASING_MATERIAL, inv.getStackInSlot(row * inv.getWidth() + col));

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
    public ItemStack getCraftingResult(CraftingInventory inv) {
        BeddingMaterial beddingId = DogBedUtil.getBeddingFromStack(DoggyTalentsAPI.BEDDING_MATERIAL, inv.getStackInSlot(1));
        CasingMaterial casingId = DogBedUtil.getCasingFromStack(DoggyTalentsAPI.CASING_MATERIAL, inv.getStackInSlot(0));

        return DogBedUtil.createItemStack(casingId, beddingId);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

    //Is on a 3x3 grid or bigger
    @Override
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
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