package doggytalents.inventory.recipe;

import doggytalents.ModRecipes;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * 1.12 Code
 */
public class RecipeDogBed extends IRecipeHidden implements IShapedRecipe {

	public RecipeDogBed(ResourceLocation resource) {
		super(resource);
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		if(!(inv instanceof InventoryCrafting)) {
	         return false;
		}
		BedMaterial beddingId = null;
		BedMaterial casingId = null;
		
		boolean beddingSel = false;
		boolean casingSel = false;
		
        for(int col = 0; col < 3; ++col) {
            for(int row = 0; row < 3; ++row) {
            	if((col == 1 && row == 0) || (col == 1 && row == 1)) {
            		BedMaterial id = DogBedRegistry.BEDDINGS.getIdFromCraftingItem(inv.getStackInSlot(col + row * inv.getWidth()));
            		if(id == BedMaterial.NULL || (!id.equals(beddingId) && beddingSel))
            			return false;
                		
            		beddingSel = true;
            		beddingId = id;

            	}
            	else {
            		BedMaterial id = DogBedRegistry.CASINGS.getIdFromCraftingItem(inv.getStackInSlot(col + row * inv.getWidth()));
            		if(id == BedMaterial.NULL || (!id.equals(casingId) && casingSel))
                		return false;
                		
            		casingSel = true;
            		casingId = id;
            	}
            }
        }

        return true;
    }

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		BedMaterial beddingId = DogBedRegistry.BEDDINGS.getIdFromCraftingItem(inv.getStackInSlot(1));
		BedMaterial casingId = DogBedRegistry.CASINGS.getIdFromCraftingItem(inv.getStackInSlot(0));
		
		return DogBedRegistry.createItemStack(casingId, beddingId);
    }

	@Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

	@Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
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


	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.DOG_BED;
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