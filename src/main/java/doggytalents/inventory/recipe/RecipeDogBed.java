package doggytalents.inventory.recipe;

import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * 1.12 Code
 */
public class RecipeDogBed extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
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
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        IBedMaterial beddingId = DogBedRegistry.BEDDINGS.getFromStack(inv.getStackInSlot(1));
        IBedMaterial casingId = DogBedRegistry.CASINGS.getFromStack(inv.getStackInSlot(0));
        
        return DogBedRegistry.createItemStack(casingId, beddingId);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    //Is on a 3x3 grid or bigger
    @Override
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }
}