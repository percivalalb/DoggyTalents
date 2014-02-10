package doggytalents.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import doggytalents.DoggyTalentsMod;
import doggytalents.api.Properties;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ConnectionHandler implements IConnectionHandler {

	public void playerLoggedIn(Player par1, NetHandler netHandler, INetworkManager manager) {
		EntityPlayer player = (EntityPlayer)par1;
		
		if (Properties.isStartingItemEnabled && !player.getEntityData().getBoolean("hasDTStartingItems")) {
            ItemStack book = new ItemStack(Item.writtenBook);
            book.stackTagCompound = new NBTTagCompound();
            NBTTagList pages = new NBTTagList();
            pages.appendTag(new NBTTagString("", "Doggy Talents\n" + 
            									  "\n" +
            									  "By RaustBlackDragon ProPercivalalb\n"));
            
            pages.appendTag(new NBTTagString("", "Ever since beta 1.4 introduced wolves to Minecraft, many players, myself included, have fallen in love with the little guys. However, even their biggest fans quickly realized that they were notoriously difficult to heal, incredibly fragile, and prone to jumping in\n"));
            pages.appendTag(new NBTTagString("", "lava. Thus most wolf owners found themselves keeping their pets indoors pretty much all of the time, serving as little more than living furniture since that was pretty much the only way they could be safe. \n"));
            
            book.stackTagCompound.setTag("pages", pages);
            book.stackTagCompound.setString("title", "Doggy Talents");
            book.stackTagCompound.setString("author", "Raust & Percivalalb");
            
            player.getEntityData().setBoolean("hasDTStartingItems", true);
            //player.inventory.addItemStackToInventory(book);
            player.inventory.addItemStackToInventory(new ItemStack(DoggyTalentsMod.doggyCharm));
            player.inventory.addItemStackToInventory(new ItemStack(DoggyTalentsMod.commandEmblem));
        }
	}

	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)  {
		return null;
	}

	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {

	}

	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {

	}

	public void connectionClosed(INetworkManager manager) {

	}

	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {

	}

}
