package doggytalents.base.b;

import doggytalents.entity.EntityDog;
import doggytalents.item.ItemRadar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRadarWrapper extends ItemRadar {

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		if(!worldIn.isRemote) {
			for(Entity entity : worldIn.loadedEntityList) {
				if(entity instanceof EntityDog) {
					EntityDog dog = (EntityDog)entity;
	
					if(dog.hasRadarCollar() && dog.canInteract(playerIn)) {
						StringBuilder builder = new StringBuilder();
						builder.append(dog.getName());
						builder.append(" is ");
						builder.append((int)Math.ceil(dog.getDistanceToEntity(playerIn)));
						builder.append(" blocks away ");
						if(playerIn.posZ > dog.posZ)
							builder.append("north");
						else
							builder.append("south");
						
						if(playerIn.posX < dog.posX)
							builder.append(", east");
						else
								builder.append(", west");
						//TODO playerIn.addChatMessage(ChatUtil.getChatComponent(builder.toString()));
					}
				}
			}
		}
		
    	return stack;
    }
}
