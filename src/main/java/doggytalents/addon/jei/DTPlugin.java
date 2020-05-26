package doggytalents.addon.jei;

import doggytalents.ModBlocks;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class DTPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModIds.JEI_ID, "doggytalents");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModBlocks.DOG_BED.get().asItem(), itemStack -> {
            if(itemStack.hasTag() && itemStack.getTag().contains("doggytalents")) {
                CompoundNBT tag = itemStack.getTag().getCompound("doggytalents");

                IBedMaterial casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId"));
                IBedMaterial beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId"));

                return casingId.getSaveId() + "+" + beddingId.getSaveId();
            }

            return "missing+missing";
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
    }
}
