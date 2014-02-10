package doggytalents.core.handler;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

/**
 * @author ProPercivalalb
 **/
public class EventRightClickEntity {
	
	@ForgeSubscribe
	public void rightClickEntity(EntityInteractEvent event) {
		 if(event.target instanceof EntityWolf && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().itemID == ModItems.trainingTreat.itemID) {
			 EntityWolf wolf = (EntityWolf)event.target;
			 if(wolf.isTamed() && wolf.getOwnerName().equals(event.entityPlayer.getCommandSenderName())) {
				event.target.setDead();
			 	World worldObj = event.entityPlayer.worldObj;
			 	EntityDTDoggy dog = new EntityDTDoggy(worldObj);
			 	dog.setTamed(true);
			 	dog.setOwner(event.entityPlayer.username);
			 	dog.setHealth(dog.getMaxHealth());
			 	dog.setSitting(false);
			 	dog.setPositionAndRotation(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
				if(!event.entityPlayer.capabilities.isCreativeMode) {
					event.entityPlayer.getCurrentEquippedItem().stackSize--;
    			}

                if (event.entityPlayer.getCurrentEquippedItem().stackSize <= 0) {
                	event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, null);
                }
			 	if(!event.target.worldObj.isRemote) {
			 		worldObj.spawnEntityInWorld(dog);
			 	}
			 }
		 }
	}
}
