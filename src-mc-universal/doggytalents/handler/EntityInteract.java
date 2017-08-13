package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class EntityInteract {
	
	public static void rightClickEntity(World world, EntityPlayer player, ItemStack stack, Entity target) {
		
	 	if(!world.isRemote) {
			
			if(target instanceof EntityWolf && stack.getItem() == ModItems.TRAINING_TREAT) {
				EntityWolf wolf = (EntityWolf)target;
				 
				if(!wolf.isDead && wolf.isTamed() && wolf.isOwner(player)) {
	
					if(!player.capabilities.isCreativeMode)
						ObjectLib.STACK_UTIL.shrink(stack, 1);
					 
				 	EntityDog dog = ObjectLib.createDog(world);
				 	dog.setTamed(true);
				 	dog.setOwnerUUID(player.getUniqueID());
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
