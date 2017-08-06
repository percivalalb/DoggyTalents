package doggytalents.item;

import doggytalents.entity.EntityDog;
import doggytalents.helper.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemDT {
	
	public ItemRadar() {
		super();
		this.setMaxStackSize(1);
	}
	
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
						playerIn.addChatMessage(ChatUtil.getChatComponent(builder.toString()));
					}
				}
			}
		}
		
    	return stack;
    }
}
