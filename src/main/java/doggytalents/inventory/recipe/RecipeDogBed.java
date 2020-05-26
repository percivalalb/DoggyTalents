package doggytalents.inventory.recipe;

import doggytalents.ModRecipes;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class RecipeDogBed extends SpecialRecipe implements IShapedRecipe<CraftingInventory> {

    public RecipeDogBed(ResourceLocation resource) {
        super(resource);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        IBedMaterial beddingId = IBedMaterial.NULL;
        IBedMaterial casingId = IBedMaterial.NULL;

        for(int col = 0; col < 3; ++col) {
            for(int row = 0; row < 3; ++row) {
                if((col == 1 && row == 0) || (col == 1 && row == 1)) {
                    IBedMaterial id = DogBedRegistry.BEDDINGS.getFromStack(inv.getStackInSlot(col + row * inv.getWidth()));
                    if(id == IBedMaterial.NULL || (beddingId != IBedMaterial.NULL && id != beddingId))
                        return false;

                    beddingId = id;
                }
                else {
                    IBedMaterial id = DogBedRegistry.CASINGS.getFromStack(inv.getStackInSlot(col + row * inv.getWidth()));
                    if(id == IBedMaterial.NULL || (beddingId != IBedMaterial.NULL && id != casingId))
                        return false;

                    casingId = id;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        IBedMaterial beddingId = DogBedRegistry.BEDDINGS.getFromStack(inv.getStackInSlot(1));
        IBedMaterial casingId = DogBedRegistry.CASINGS.getFromStack(inv.getStackInSlot(0));

        return DogBedRegistry.createItemStack(casingId, beddingId);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
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
        return ModRecipes.DOG_BED.get();
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