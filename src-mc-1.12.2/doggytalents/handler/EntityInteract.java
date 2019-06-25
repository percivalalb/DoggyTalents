package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 **/
public class EntityInteract {
    
    @SubscribeEvent
    public void rightClickEntity(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();
        
         if(!world.isRemote) {
            
            if(target instanceof EntityWolf && !stack.isEmpty() && stack.getItem() == ModItems.TRAINING_TREAT) {
                EntityWolf wolf = (EntityWolf)target;
                 
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
