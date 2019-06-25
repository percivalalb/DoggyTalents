package doggytalents.addon.jei;

import doggytalents.ModBlocks;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

@JEIPlugin
public class DTPlugin implements IModPlugin {
    
    @Override
    public void registerItemSubtypes(ISubtypeRegistry registration) {
        registration.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.DOG_BED), itemStack -> {
            if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("doggytalents")) {
                NBTTagCompound tag = itemStack.getTagCompound().getCompoundTag("doggytalents");
                return tag.getString("casingId") + ":" + tag.getString("beddingId");
            }
            
            return "missing:missing";
        });
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        
    }
}
