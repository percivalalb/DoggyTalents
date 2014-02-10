package doggytalents.core.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author ProPercivalalb
 */
public class ItemStackHelper {

	/**
	 * Converts an object to and ItemStack. Check to see whether it is 
	 * not null before using the returned object.
	 * @param obj A data type that can be converted to and ItemStack
	 * @return An ItemStack created from the given object.
	 */
	 public static ItemStack convertObjectToItemStack(Object obj) {
		 if (obj instanceof Item)
	         return new ItemStack((Item)obj);
	     else if (obj instanceof Block)
	         return new ItemStack((Block)obj);
	     else if (obj instanceof ItemStack)
	         return ((ItemStack)obj).copy();
	     else if (obj instanceof Integer)
	         return new ItemStack((Integer)obj, 1, 0);
	     else
	    	 return null;
	 }
	 
	 /**
	  * Checks whether the item has and tag compound if not it creates a new 
	  * one for that itemstack.
	  * @param item The item needed to be checked for a #NBTTagCompound
	  * @return Returns whether it has created a new #NBTTagCompound
	  */
	 public static boolean makeSureItemHasTagCompound(ItemStack item) {
		 if(!item.hasTagCompound()) {
			 item.setTagCompound(new NBTTagCompound());
			 return true;
		 }
		 return false;
	 }
	 
	 public static boolean isItem(ItemStack item, Item itemClass) {
		 if(item == null || itemClass == null) return false;
		 return item.itemID == itemClass.itemID;
	 }
	 
	 public static boolean isItem(ItemStack item, int id) {
		 if(item == null) return false;
		 return item.itemID == id;
	 }
	 
	 public static void setDamage(ItemStack item, int damage) {
		 item.setItemDamage(damage);
	 }
	 
	 public static boolean hasTag(ItemStack itemStack, String keyName) {
	        if (itemStack.stackTagCompound != null)
	            return itemStack.stackTagCompound.hasKey(keyName);
	        return false;
	 }

	 public static void removeTag(ItemStack itemStack, String keyName) {
		 if (itemStack.stackTagCompound != null) {
			 itemStack.stackTagCompound.removeTag(keyName);
	     }
	 }

	 //String
	 public static String getString(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setString(itemStack, keyName, "");
	     }
	     return itemStack.stackTagCompound.getString(keyName);
	 }

	 public static void setString(ItemStack itemStack, String keyName, String keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setString(keyName, keyValue);
	 }

	 //Boolean
	 public static boolean getBoolean(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setBoolean(itemStack, keyName, false);
	     }
	     return itemStack.stackTagCompound.getBoolean(keyName);
	 }

	 public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setBoolean(keyName, keyValue);
	 }

	 //Byte
	 public static byte getByte(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setByte(itemStack, keyName, (byte) 0);
	     }
	     return itemStack.stackTagCompound.getByte(keyName);
	 }

	 public static void setByte(ItemStack itemStack, String keyName, byte keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setByte(keyName, keyValue);
	 }

	 //Short
	 public static short getShort(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setShort(itemStack, keyName, (short) 0);
	     }
	     return itemStack.stackTagCompound.getShort(keyName);
	 }

	 public static void setShort(ItemStack itemStack, String keyName, short keyValue) {
	    makeSureItemHasTagCompound(itemStack);
	    itemStack.stackTagCompound.setShort(keyName, keyValue);
	 }

	 //Integer
	 public static int getInt(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setInteger(itemStack, keyName, 0);
	     }
	     return itemStack.stackTagCompound.getInteger(keyName);
	 }

	 public static void setInteger(ItemStack itemStack, String keyName, int keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setInteger(keyName, keyValue);
	 }

	 //Long
	 public static long getLong(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setLong(itemStack, keyName, 0);
	     }
	     return itemStack.stackTagCompound.getLong(keyName);
	 }

	 public static void setLong(ItemStack itemStack, String keyName, long keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setLong(keyName, keyValue);
	 }

	 //Float
	 public static float getFloat(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setFloat(itemStack, keyName, 0);
	     }
	     return itemStack.stackTagCompound.getFloat(keyName);
	 }

	 public static void setFloat(ItemStack itemStack, String keyName, float keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setFloat(keyName, keyValue);
	 }

	 //Double
	 public static double getDouble(ItemStack itemStack, String keyName) {
		 makeSureItemHasTagCompound(itemStack);
	     if (!itemStack.stackTagCompound.hasKey(keyName)) {
	    	 setDouble(itemStack, keyName, 0);
	     }
	     return itemStack.stackTagCompound.getDouble(keyName);
	 }

	 public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setDouble(keyName, keyValue);
	 }
	 
	 //Tag
	 public static NBTBase getTag(ItemStack itemStack, String keyName) {
		 if (!itemStack.stackTagCompound.hasKey(keyName)) {
			 return null;
		 }
		 return itemStack.stackTagCompound.getTag(keyName);
	 }
	 
	 public static void setTag(ItemStack itemStack, String keyName, NBTBase keyValue) {
		 makeSureItemHasTagCompound(itemStack);
	     itemStack.stackTagCompound.setTag(keyName, keyValue);
	 }
	 
	 public static ItemStack consumeItem(ItemStack stack) {
		 if (stack.stackSize == 1) {
			if (stack.getItem().hasContainerItem())
				return stack.getItem().getContainerItemStack(stack);
			else {
				return null;
			}
		} 
		else {
			stack.splitStack(1);
			return stack;
		}
	}
}
