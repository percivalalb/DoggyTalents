package doggytalents.addon.jei;

import doggytalents.ModBlocks;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class DTPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(ModIds.JEI_ID, "doggytalents");
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(ModBlocks.DOG_BED.asItem(), itemStack -> {
			if(itemStack.hasTag() && itemStack.getTag().contains("doggytalents")) {
				NBTTagCompound tag = itemStack.getTag().getCompound("doggytalents");
			    
				BedMaterial casingId = DogBedRegistry.CASINGS.getFromString(tag.getString("casingId"));
		    	BedMaterial beddingId = DogBedRegistry.BEDDINGS.getFromString(tag.getString("beddingId"));
			    
		    	return casingId.key + ":" + beddingId.key;
			}
			
			return "missing:missing";
		});
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
	}
}
