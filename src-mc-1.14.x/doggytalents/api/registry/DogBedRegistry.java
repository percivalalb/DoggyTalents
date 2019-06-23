package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.helper.Compatibility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry {

	public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
	public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");
	
	private final List<BedMaterial> keys = new ArrayList<BedMaterial>();
	private final String key;
	
	public DogBedRegistry(String key) {
		this.key = key;
	}
	
	public IBedMaterial registerMaterial(@Nonnull Block block, ResourceLocation textureLocation) {
		return this.registerMaterial(new BedMaterial(block, textureLocation, Ingredient.fromItems(block)));
	}
	
	public BedMaterial registerMaterial(BedMaterial material) {
		if(this.keys.contains(material)) {
			DoggyTalentsMod.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", material); 
			return null;
		}
		else {
			this.keys.add(material.setRegName(this.key));
			DoggyTalentsMod.LOGGER.info("Register dog bed {} under the key {}", this.key, material);
			return material;
		}
	}
	
	public List<BedMaterial> getKeys() {
		return this.keys;
	}
	
	public IBedMaterial getFromString(String key) {
		if(key.equals("missing"))
			return IBedMaterial.MISSING;
		
		// Keep things when updating from 1.12
		key = Compatibility.getBedOldNamingScheme(key);
		
		for(IBedMaterial thing : this.keys) {
			if(thing.getSaveId().equals(key)) {
				return thing;
			}
		}
		return IBedMaterial.MISSING;
	}
	
	public IBedMaterial getIdFromCraftingItem(ItemStack stack) {
	    for(IBedMaterial m : this.keys) {
			if(m.getIngredients().test(stack))
				return m;
		}
		return IBedMaterial.MISSING;
	}
	
	public static ItemStack createItemStack(IBedMaterial casingId, IBedMaterial beddingId) {
		ItemStack stack = new ItemStack(ModBlocks.DOG_BED, 1);
		stack.setTag(new CompoundNBT());
		
		CompoundNBT tag = new CompoundNBT();
		tag.putString("casingId", casingId.getSaveId());
		tag.putString("beddingId", beddingId.getSaveId());
		stack.getTag().put("doggytalents", tag);
		return stack;
	}
}
