package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 **/
public class EntityInteractHandler {
	
	@SubscribeEvent
	public void rightClickEntity(EntityInteract event) {
	 	World world = event.getTarget().world;
		
	 	if(!world.isRemote) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
			
			if(event.getTarget() instanceof EntityWolf && stack.getItem() == ModItems.trainingTreat) {
				EntityWolf wolf = (EntityWolf)event.getTarget();
				 
				if(!wolf.isDead && wolf.isTamed() && wolf.isOwner(player)) {
	
					if(!player.capabilities.isCreativeMode)
		        		stack.shrink(1);
					 
				 	EntityDog dog = new EntityDog(world);
				 	dog.setTamed(true);
				 	dog.setOwnerId(player.getUniqueID());
				 	dog.setHealth(dog.getMaxHealth());
				 	dog.setSitting(false);
				 	dog.setGrowingAge(wolf.getGrowingAge());
				 	dog.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
				 
				 	world.spawnEntity(dog);
				 	
					wolf.setDead();
					
				 }
			 }
	 	}
	}
}
