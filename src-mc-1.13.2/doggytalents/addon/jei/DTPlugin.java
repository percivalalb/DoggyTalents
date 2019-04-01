package doggytalents.addon.jei;

import java.util.ArrayList;
import java.util.List;

import doggytalents.ModBlocks;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.item.Item;
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
		registration.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.DOG_BED), itemStack -> {
			List<String> enchantmentNames = new ArrayList<>();
			
			if(itemStack.hasTag() && itemStack.getTag().contains("doggytalents")) {
				NBTTagCompound tag = itemStack.getTag().getCompound("doggytalents");
			    
				BedMaterial casingId = DogBedRegistry.CASINGS.getFromString(tag.getString("casingId"));
		    	BedMaterial beddingId = DogBedRegistry.BEDDINGS.getFromString(tag.getString("beddingId"));
			    
		    	String caseUid = casingId.key + ":" + beddingId.key;
		    	
		    	enchantmentNames.add(caseUid);
			}
			
			enchantmentNames.sort(null);
			return enchantmentNames.toString();
		});
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(DogBedRecipeMaker.createDogBedRecipes(), VanillaRecipeCategoryUid.CRAFTING);
	}
}
