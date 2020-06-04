package doggytalents.common.addon.jei;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.DoggyBlocks;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.common.util.DogBedUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class DTPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModIds.JEI_ID, "doggytalents");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(DoggyBlocks.DOG_BED.get().asItem(), stack -> {
            Pair<IBedMaterial, IBedMaterial> materials = DogBedUtil.getMaterials(stack);

            return materials.getLeft().getSaveId() + "+" + materials.getRight().getSaveId();
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
    }
}
