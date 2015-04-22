package doggytalents.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import doggytalents.ModItems;
import doggytalents.lib.Constants;

/**
 * @author ProPercivalalb
 */
public class ConnectionHandler {

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		NBTTagCompound entityData = player.getEntityData();

		if (Constants.STARTING_ITEMS && !entityData.getBoolean("gotDTStartingItems")) {
            entityData.setBoolean("gotDTStartingItems", true);

            entityData.setBoolean("gotNoDTStartingItems", false);
            //player.inventory.addItemStackToInventory(book);
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.doggyCharm));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.commandEmblem));
        }
	}
}
