package doggytalents.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

/**
 * @author ProPercivalalb
 **/
public class EntityInteract {
	
	@SubscribeEvent
	public void rightClickEntity(EntityInteractEvent event) {
	 	World world = event.target.worldObj;
		
	 	if(!world.isRemote) {
			EntityPlayer player = event.entityPlayer;
			ItemStack stack = player.getHeldItem();
			
			if(event.target instanceof EntityWolf && stack.getItem() == ModItems.TRAINING_TREAT) {
				EntityWolf wolf = (EntityWolf)event.target;
				 
				if(!wolf.isDead && wolf.isTamed() && wolf.func_152114_e(player)) {
	
					if(!player.capabilities.isCreativeMode)
						--stack.stackSize;
					 
				 	EntityDog dog = new EntityDog(world);
				 	dog.setTamed(true);
				 	dog.func_152115_b(player.getUniqueID().toString());
				 	dog.setHealth(dog.getMaxHealth());
				 	dog.setSitting(false);
				 	dog.setGrowingAge(wolf.getGrowingAge());
				 	dog.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
				 
				 	world.spawnEntityInWorld(dog);
				 	
					wolf.setDead();
					
				 }
			 }
	 	}
	}
}
