package doggytalents.core.handler;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.ModItems;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 **/
public class EventRightClickEntity {
	
	@SubscribeEvent
	public void rightClickEntity(EntityInteractEvent event) {
		 if(event.target instanceof EntityWolf && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == ModItems.trainingTreat) {
			 EntityWolf wolf = (EntityWolf)event.target;
			 if(wolf.isTamed() && wolf.func_152114_e(event.entityPlayer)) {
				event.target.setDead();
			 	World worldObj = event.entityPlayer.worldObj;
			 	EntityDTDoggy dog = new EntityDTDoggy(worldObj);
			 	dog.setTamed(true);
			 	dog.func_152115_b(event.entityPlayer.getUniqueID().toString());
			 	dog.setHealth(dog.getMaxHealth());
			 	dog.setSitting(false);
			 	dog.setGrowingAge(wolf.getGrowingAge());
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
