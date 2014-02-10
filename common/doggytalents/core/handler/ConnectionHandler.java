package doggytalents.core.handler;

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

		if (Constants.isStartingItemEnabled && entityData.getBoolean("gotNoDTStartingItems")) {
            ItemStack book = new ItemStack(Items.written_book);
            book.stackTagCompound = new NBTTagCompound();
            NBTTagList pages = new NBTTagList();
            pages.appendTag(new NBTTagString("Doggy Talents\n" + 
            									  "\n" +
            									  "By RaustBlackDragon ProPercivalalb\n"));
            
            pages.appendTag(new NBTTagString("Ever since beta 1.4 introduced wolves to Minecraft, many players, myself included, have fallen in love with the little guys. However, even their biggest fans quickly realized that they were notoriously difficult to heal, incredibly fragile, and prone to jumping in\n"));
            pages.appendTag(new NBTTagString("lava. Thus most wolf owners found themselves keeping their pets indoors pretty much all of the time, serving as little more than living furniture since that was pretty much the only way they could be safe. \n"));
            
            book.stackTagCompound.setTag("pages", pages);
            book.stackTagCompound.setString("title", "Doggy Talents");
            book.stackTagCompound.setString("author", "Raust & Percivalalb");
            
            entityData.setBoolean("gotNoDTStartingItems", false);
            //player.inventory.addItemStackToInventory(book);
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.doggyCharm));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.commandEmblem));
        }
	}
}
