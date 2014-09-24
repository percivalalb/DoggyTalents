package doggytalents.api;

import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.ModBlocks;

/**
 * @author ProPercivalalb
 */
public class DogBedManager {

	private static Hashtable<String, IDogBedMaterial> woolIcons = new Hashtable<String, IDogBedMaterial>();
	private static Hashtable<String, IDogBedMaterial> woodIcons = new Hashtable<String, IDogBedMaterial>();
	private static Hashtable<String, ItemStack> woolAssociatedItem = new Hashtable<String, ItemStack>();
	private static Hashtable<String, ItemStack> woodAssociatedItem = new Hashtable<String, ItemStack>();
	private static boolean canRegister = false;
	
	public static boolean isValidWoodId(String woodId) {
		return woodIcons.keySet().contains(woodId);
	}
	
	public static boolean isValidWoolId(String woolId) {
		return woolIcons.keySet().contains(woolId);
	}
	
	public static IIcon getWoodIcon(String woodId, int side) {
		if(isValidWoodId(woodId))
			return woodIcons.get(woodId).getMaterialIcon(side);
		return null;
	}
	
	public static IIcon getWoolIcon(String woolId, int side) {
		if(isValidWoolId(woolId))
			return woolIcons.get(woolId).getMaterialIcon(side);
		return null;
	}
	
	public static String[] getAllWoodIds() {
		return woodIcons.keySet().toArray(new String[woodIcons.size()]);
	}
	
	public static String[] getAllWoolIds() {
		return woolIcons.keySet().toArray(new String[woolIcons.size()]);
	}
	
	public static void registerBedWood(ItemStack plank) {
		Block plankBlock = Block.getBlockFromItem(plank.getItem());
		DogBedManager.registerBedWood(plank.getDisplayName(), new DefaultBedMaterial(plankBlock, plank.getItemDamage()), plank);
	}

	/**
	 *
	 * @param uniqueId A save id that will be used to look up the icon
	 * @param iconLookup The pre-registered icon stored in a hashtable
	 */
	public static void registerBedWood(String uniqueId, IDogBedMaterial iconLookup, ItemStack associatedItem) {
		if (associatedItem == null || associatedItem.getItem() == null) {
			FMLLog.warning("[DoggyTalents] The wood id '%s' does not exist, please fix", uniqueId);
			return;
		}

		if(woodIcons.keySet().contains(uniqueId)) {
			FMLLog.warning("[DoggyTalents] The dog bed wood id '%s' has already been taken, please fix", uniqueId);
			return;
		}
		FMLLog.info("Registered dog bed wood id '%s'", uniqueId);
		woodIcons.put(uniqueId, iconLookup);
		woodAssociatedItem.put(uniqueId, associatedItem);
		for(String woolId : DogBedManager.getAllWoolIds()) {
			GameRegistry.addRecipe(createItemStack(uniqueId, woolId), new Object[] {"CBC", "CBC", "CCC", 'C', associatedItem, 'B', woolAssociatedItem.get(woolId)});
		}
	}
	
	/**
	 *
	 * @param uniqueId A save id that will be used to look up the icon
	 * @param iconLookup The pre-registered icon stored in a hashtable
	 */
	public static void registerBedWool(String uniqueId, IDogBedMaterial iconLookup, ItemStack associatedItem) {
		if(woolIcons.keySet().contains(uniqueId)) {
			FMLLog.warning("[DoggyTalents] The dog bed wool id '%s' has already been taken, please fix");
			return;
		}
		FMLLog.info("Registered dog bed wool id '%s'", uniqueId);
		woolIcons.put(uniqueId, iconLookup);
		woolAssociatedItem.put(uniqueId, associatedItem);
		for(String woodId : DogBedManager.getAllWoodIds()) {
			GameRegistry.addRecipe(createItemStack(woodId, uniqueId), new Object[] {"CCC", "CBC", "CBC", 'C', woodAssociatedItem.get(woodId), 'B', associatedItem});
		}
	}
	
	public static ItemStack createItemStack(String woodId, String woolId) {
		ItemStack stack = new ItemStack(ModBlocks.dogBed, 1, 0);
		stack.stackTagCompound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("woodId", woodId);
		tag.setString("woolId", woolId);
		stack.stackTagCompound.setTag("doggytalents", tag);
		return stack;
	}
}
