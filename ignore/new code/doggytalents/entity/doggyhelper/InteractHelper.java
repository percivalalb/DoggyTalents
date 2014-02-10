package doggytalents.entity.doggyhelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import doggytalents.DoggyTalentsMod;
import doggytalents.core.helper.ItemStackHelper;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class InteractHelper {

	public static boolean interact(EntityDTDoggy dog, EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
        return !dog.isTamed() ? handleUnTamed(dog, player, itemstack) : handleTamed(dog, player, itemstack);
	}
	
	public static boolean handleUnTamed(EntityDTDoggy dog, EntityPlayer player, ItemStack itemstack) {
		if (itemstack != null && itemstack.itemID == Item.bone.itemID && !dog.isAngry()) {
			itemstack = ItemStackHelper.consumeItem(itemstack);
            
            if (!dog.worldObj.isRemote) {
                if (dog.getRNG().nextInt(3) == 0) {
                	dog.setIsTamed(true);
                    dog.setPathToEntity(null);
                    dog.setIsSitting(true);
                    dog.setEntityHealth(20);
                    dog.setOwner(player.username);
                    dog.showHeartsOrSmokeFX(true);
                    dog.worldObj.setEntityState(dog, (byte)7);
                }
                else {
                	dog.showHeartsOrSmokeFX(false);
                	dog.worldObj.setEntityState(dog, (byte)6);
                }
            }
            return true;
        }
		return false;
	}
	
	public static boolean handleTamed(EntityDTDoggy dog, EntityPlayer player, ItemStack itemstack) {
		 if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood)) {
             ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];
             if (itemfood.isWolfsFavoriteMeat() && dog.getDataWatcher().getWatchableObjectInt(18) < 20) {
                 dog.heal(itemfood.getHealAmount());
                 itemstack = ItemStackHelper.consumeItem(itemstack);
                 return true;
             }
         }
		 
		 if (player.username.equalsIgnoreCase(dog.getOwner())) {
             player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_MAIN_MENU, dog.worldObj, dog.entityId, 0, 0);
             return true;
         }
		 
         if (player.username.equalsIgnoreCase(dog.getOwner())) {
             if (!dog.worldObj.isRemote) {
             	dog.setIsSitting(!dog.isSitting());
                 dog.isJumping = false;
                 dog.setPathToEntity(null);
             }
             return true;
         }
         return false;
	}
}
