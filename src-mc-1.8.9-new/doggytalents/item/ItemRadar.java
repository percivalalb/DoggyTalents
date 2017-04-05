package doggytalents.item;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;
import doggytalents.helper.ChatHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemDT {
	
	public ItemRadar() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

		if(!worldIn.isRemote)
		for(Object entity : worldIn.loadedEntityList) {
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
					playerIn.addChatMessage(ChatHelper.getChatComponent(builder.toString()));
				}
			}
		}
    	return itemStackIn;
    }
}
