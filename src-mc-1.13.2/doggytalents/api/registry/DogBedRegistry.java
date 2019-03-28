package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry {

	public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
	public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");
	
	private final List<String> keys = new ArrayList<String>();
	private final Map<String, String> lookupnames = new HashMap<String, String>();
	private final Map<String, String> textures = new HashMap<String, String>();
	private final Map<String, CustomIngredient> craftingItems = new HashMap<String, CustomIngredient>();
	private final String key;
	
	public DogBedRegistry(String key) {
		this.key = key;
	}
	
	public boolean isValidId(String id) {
		return this.keys.contains(id);
	}
	
	public void registerMaterial(String blockId, String textureLocation) {
		//if(!Block.REGISTRY.containsKey(new ResourceLocation(blockId)))
		//	DoggyTalentsMod.LOGGER.warn("The block id {} does not exist for a material", blockId);
		//else {
			Block block = IRegistry.BLOCK.get(new ResourceLocation(blockId));
			String lookupname = String.format("dogbed.%s.%s", this.key, blockId);
			ItemStack stack = new ItemStack(block, 1);
			this.registerMaterial(blockId, lookupname, textureLocation, stack);
		//}
	}
	
	public void registerMaterial(Block block, String textureLocation) {
		if(block == null) {
			DoggyTalentsMod.LOGGER.warn("Null block cannot be registered for a material");
			return;
		}
		
		String blockId = IRegistry.BLOCK.getKey(block).toString();
		String lookupname = String.format("dogbed.%s.%s", this.key, blockId);
		ItemStack stack = new ItemStack(block, 1);
		this.registerMaterial(blockId, lookupname, textureLocation, stack);
	}
	
	public void registerMaterial(String key, String lookupname, String textureLocation, ItemStack craftingItem) {
		if(this.isValidId(key))
			DoggyTalentsMod.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", key); 
		else {
			this.keys.add(key);
			this.lookupnames.put(key, lookupname);
			this.textures.put(key, textureLocation);
			this.craftingItems.put(key, CustomIngredient.fromStacks(craftingItem));
			
			DoggyTalentsMod.LOGGER.info("Register dog bed {} under the key {}", this.key, key);
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
	
	public String getIdFromCraftingItem(ItemStack stack) {
		for(Entry<String, CustomIngredient> entry : craftingItems.entrySet()) {
			if(entry.getValue().apply(stack))
				return entry.getKey();
		}
		return "";
	}
	
	public static ItemStack createItemStack(String casingId, String beddingId) {
		ItemStack stack = new ItemStack(ModBlocks.DOG_BED, 1);
		stack.setTag(new NBTTagCompound());
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.putString("casingId", casingId);
		tag.putString("beddingId", beddingId);
		stack.getTag().put("doggytalents", tag);
		return stack;
	}
}
