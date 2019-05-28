package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.block.PropertyMaterial;
import doggytalents.helper.Compatibility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry {

	public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
	public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");
	
	private final List<BedMaterial> keys = new ArrayList<BedMaterial>();
	private final Map<BedMaterial, String> lookupnames = new HashMap<BedMaterial, String>();
	private final Map<BedMaterial, String> textures = new HashMap<BedMaterial, String>();
	private final Map<BedMaterial, CustomIngredient> craftingItems = new HashMap<BedMaterial, CustomIngredient>();
	private final String key;
	
	public DogBedRegistry(String key) {
		this.key = key;
	}
	
	public BedMaterial registerMaterial(@Nonnull String blockId, String textureLocation) {
		ResourceLocation resource = new ResourceLocation(blockId);
		Block block = ForgeRegistries.BLOCKS.getValue(resource);
		String lookupname = String.format("dogbed.%s.%s.%s", this.key, resource.getNamespace(), resource.getPath());
		ItemStack stack = new ItemStack(block, 1);
		BedMaterial thing = new BedMaterial(blockId);
		return this.registerMaterial(thing, lookupname, textureLocation, stack);
	}
	
	public BedMaterial registerMaterial(@Nonnull Block block, String textureLocation) {
		ResourceLocation resource = ForgeRegistries.BLOCKS.getKey(block);
		String blockId = resource.getNamespace() + "_" + resource.getPath();
		String lookupname = String.format("dogbed.%s.%s.%s", this.key, resource.getNamespace(), resource.getPath());
		ItemStack stack = new ItemStack(block, 1);
		BedMaterial thing = new BedMaterial(blockId);
		return this.registerMaterial(thing, lookupname, textureLocation, stack);
	}
	
	public BedMaterial registerMaterial(BedMaterial key, String lookupname, String textureLocation, ItemStack craftingItem) {
		if(this.isValidId(key)) {
			DoggyTalentsMod.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", key); 
			return null;
		}
		else {
			this.keys.add(key);
			this.lookupnames.put(key, lookupname);
			this.textures.put(key, textureLocation);
			this.craftingItems.put(key, CustomIngredient.fromStacks(craftingItem));
			
			DoggyTalentsMod.LOGGER.info("Register dog bed {} under the key {}", this.key, key);
			return key;
		}
	}
	
	public boolean isValidId(BedMaterial id) {
		return this.keys.contains(id);
	}
	
	public List<BedMaterial> getKeys() {
		return this.keys;
	}
	
	public BedMaterial getFromString(String key) {
		if(key.equals("missing"))
			return BedMaterial.NULL;
		
		// Keep things when updating from 1.12
		key = Compatibility.getBedOldNamingScheme(key);
		
		for(BedMaterial thing : this.keys) {
			if(thing.key.equals(key)) {
				return thing;
			}
		}
		return BedMaterial.NULL;
	}
	
	public String getTranslationKey(BedMaterial id) {
		if(!this.isValidId(id))
			return "missing";
		return this.lookupnames.get(id);
	}
	
	private String getTexture(BedMaterial id) {
		if(!this.isValidId(id))
			return "missing";
		return this.textures.get(id);
	}
	
	public String getTexture(IBlockState state, PropertyMaterial property) {
		return this.getTexture(state.get(property));
	}
	
	public String getTexture(NBTTagCompound nbt, String key) {
		return this.getTexture(this.getFromString(nbt.getString(key)));
	}
	
	public BedMaterial getIdFromCraftingItem(ItemStack stack) {
		for(Entry<BedMaterial, CustomIngredient> entry : craftingItems.entrySet()) {
			if(entry.getValue().apply(stack))
				return entry.getKey();
		}
		return BedMaterial.NULL;
	}
	
	public ItemStack getCraftingItemFromBedMaterial(BedMaterial material) {
		if(this.craftingItems.containsKey(material)) {
			return this.craftingItems.get(material).getIngredient();
		}
		return ItemStack.EMPTY;
	}
	
	public static ItemStack createItemStack(BedMaterial casingId, BedMaterial beddingId) {
		ItemStack stack = new ItemStack(ModBlocks.DOG_BED, 1);
		stack.setTag(new NBTTagCompound());
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.putString("casingId", casingId.key);
		tag.putString("beddingId", beddingId.key);
		stack.getTag().put("doggytalents", tag);
		return stack;
	}
}
