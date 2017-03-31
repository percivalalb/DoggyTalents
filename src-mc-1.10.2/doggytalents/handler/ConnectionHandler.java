package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * @author ProPercivalalb
 */
public class ConnectionHandler {

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		
		NBTTagCompound tag = player.getEntityData();

        if(!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
        	tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        
        NBTTagCompound persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        
		if(Constants.STARTING_ITEMS && !persistTag.getBoolean("gotDTStartingItems")) {
			persistTag.setBoolean("gotDTStartingItems", true);

            player.inventory.addItemStackToInventory(new ItemStack(ModItems.doggyCharm));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.commandEmblem));
        }
	}
}
