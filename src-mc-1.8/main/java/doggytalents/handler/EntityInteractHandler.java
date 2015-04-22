package doggytalents.handler;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import doggytalents.ModItems;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 **/
public class EntityInteractHandler {
	
	@SubscribeEvent
	public void rightClickEntity(EntityInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
	 	World world = event.target.worldObj;
		ItemStack stack = event.entityPlayer.getHeldItem();
		
		if(event.target instanceof EntityWolf && stack != null && stack.getItem() == ModItems.trainingTreat) {
			EntityWolf wolf = (EntityWolf)event.target;
			 
			if(wolf.isTamed() && wolf.isOwner(event.entityPlayer)) {

				if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
				 
			 	EntityDog dog = new EntityDog(world);
			 	dog.setTamed(true);
			 	dog.setOwnerId(event.entityPlayer.getUniqueID().toString());
			 	dog.setHealth(dog.getMaxHealth());
			 	dog.setSitting(false);
			 	dog.setGrowingAge(wolf.getGrowingAge());
			 	dog.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
			 
			 	if(!world.isRemote)
			 		world.spawnEntityInWorld(dog);
			 	
				wolf.setDead();
			 }
		 }
	}
}
