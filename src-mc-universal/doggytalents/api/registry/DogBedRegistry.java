package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry {

	public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
	public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");
	
	private final List<String> keys = new ArrayList<String>();
	private final Map<String, String> lookupnames = new HashMap<String, String>();
	private final Map<String, String> textures = new HashMap<String, String>();
	private final Map<String, Ingredient> craftingItems = new HashMap<String, Ingredient>();
	private final String key;
	
	public DogBedRegistry(String key) {
		this.key = key;
	}
	
	public boolean isValidId(String id) {
		return this.keys.contains(id);
	}
	
	public void registerMaterial(String blockId, String textureLocation) { this.registerMaterial(blockId, 0, textureLocation); }
	public void registerMaterial(Block block, String textureLocation) { this.registerMaterial(block, 0, textureLocation); }
	
	public void registerMaterial(String blockId, int meta, String textureLocation) {
		if(!Block.REGISTRY.containsKey(new ResourceLocation(blockId)))
			DoggyTalents.LOGGER.warn("The block id {} does not exist for a material", blockId);
		else {
			Block block = Block.getBlockFromName(blockId);
			String lookupname = String.format("dogbed.%s.%s.%d", this.key, blockId, meta);
			ItemStack stack = new ItemStack(block, 1, meta);
			this.registerMaterial(blockId + "." + meta, lookupname, textureLocation, stack);
		}
	}
	
	public void registerMaterial(Block block, int meta, String textureLocation) {
		String blockId = ((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString();
		String lookupname = String.format("dogbed.%s.%s.%d", this.key, blockId, meta);
		ItemStack stack = new ItemStack(block, 1, meta);
		this.registerMaterial(blockId + "." + meta, lookupname, textureLocation, stack);
	}
	
	public void registerMaterial(String key, String lookupname, String textureLocation, ItemStack craftingItem) {
		if(this.isValidId(key))
			DoggyTalents.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", key); 
		else {
			this.keys.add(key);
			this.lookupnames.put(key, lookupname);
			this.textures.put(key, textureLocation);
			this.craftingItems.put(key, Ingredient.fromStacks(craftingItem));
			
			DoggyTalents.LOGGER.info("Register dog bed {} under the key {}", this.key, key);
		}
	}
	
	public List<String> getKeys() {
		return this.keys;
	}
	
	public String getLookUpValue(String id) {
		if(!this.isValidId(id))
			return null;
		return this.lookupnames.get(id);
	}
	
	public String getTexture(String id) {
		if(!this.isValidId(id))
			return null;
		return this.textures.get(id);
	}
	
	public Ingredient getCraftingItem(String id) {
		if(!this.isValidId(id))
			return null;
		return this.craftingItems.get(id);
	}
	
	public String getIdFromCraftingItem(ItemStack stack) {
		for(Entry<String, Ingredient> entry : craftingItems.entrySet()) {
			if(entry.getValue().apply(stack))
				return entry.getKey();
		}
		return "";
	}
	
	public static ItemStack createItemStack(String casingId, String beddingId) {
		ItemStack stack = new ItemStack(ModBlocks.DOG_BED, 1, 0);
		stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("casingId", casingId);
		tag.setString("beddingId", beddingId);
		stack.getTagCompound().setTag("doggytalents", tag);
		return stack;
	}
}
