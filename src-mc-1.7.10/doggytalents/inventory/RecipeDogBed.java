package doggytalents.inventory;

import com.google.common.base.Strings;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeDogBed implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		String beddingId = "";
		String casingId = "";
		
		boolean beddingSel = false;
		boolean casingSel = false;
		
        for(int col = 0; col < 3; ++col) {
            for(int row = 0; row < 3; ++row) {
            	if((col == 1 && row == 0) || (col == 1 && row == 1)) {
            		String id = DogBedRegistry.BEDDINGS.getIdFromCraftingItem(inv.getStackInRowAndColumn(col, row));
            		if(Strings.isNullOrEmpty(id) || (!id.equals(beddingId) && beddingSel))
            			return false;
                		
            		beddingSel = true;
            		beddingId = id;

            	}
            	else {
            		String id = DogBedRegistry.CASINGS.getIdFromCraftingItem(inv.getStackInRowAndColumn(col, row));
            		if(Strings.isNullOrEmpty(id) || (!id.equals(casingId) && casingSel))
                		return false;
                		
            		casingSel = true;
            		casingId = id;
            	}
            }
        }

        return true;
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
		String beddingId = DogBedRegistry.BEDDINGS.getIdFromCraftingItem(inv.getStackInRowAndColumn(1, 0));
		String casingId = DogBedRegistry.CASINGS.getIdFromCraftingItem(inv.getStackInRowAndColumn(0, 0));
		
		return DogBedRegistry.createItemStack(casingId, beddingId);
    }

	@Override
    public ItemStack getRecipeOutput() {
        return null;
    }

	@Override
	public int getRecipeSize() {
        return 9;
    }
}