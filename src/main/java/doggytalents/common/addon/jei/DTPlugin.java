//package doggytalents.common.addon.jei;
//
//import doggytalents.DoggyBlocks;
//import doggytalents.api.registry.IBeddingMaterial;
//import doggytalents.api.registry.ICasingMaterial;
//import doggytalents.common.util.DogBedUtil;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.constants.ModIds;
//import mezz.jei.api.constants.VanillaRecipeCategoryUid;
//import mezz.jei.api.registration.IRecipeRegistration;
//import mezz.jei.api.registration.ISubtypeRegistration;
//import net.minecraft.resources.ResourceLocation;
//import org.apache.commons.lang3.tuple.Pair;
//
//@JeiPlugin
//public class DTPlugin implements IModPlugin {
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return new ResourceLocation(ModIds.JEI_ID, "doggytalents");
//    }
//
//    @Override
//    public void registerItemSubtypes(ISubtypeRegistration registration) {
//        registration.registerSubtypeInterpreter(DoggyBlocks.DOG_BED.get().asItem(), (stack, ctx) -> {
//            Pair<ICasingMaterial, IBeddingMaterial> materials = DogBedUtil.getMaterials(stack);
//
//            String casingKey = materials.getLeft() != null
//                    ? materials.getLeft().getRegistryName().toString()
//                    : "doggytalents:casing_missing";
//
//            String beddingKey = materials.getRight() != null
//                    ? materials.getRight().getRegistryName().toString()
//                    : "doggytalents:bedding_missing";
//
//            return casingKey + "+" + beddingKey;
//        });
//    }
//
//    @Override
//    public void registerRecipes(IRecipeRegistration registration) {
//        registration.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
//    }
//}
