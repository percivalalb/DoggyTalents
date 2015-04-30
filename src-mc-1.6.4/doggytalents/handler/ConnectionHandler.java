package doggytalents.handler;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import doggytalents.ModItems;
import doggytalents.lib.Constants;

/**
 * @author ProPercivalalb
 */
public class ConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player p, NetHandler netHandler, INetworkManager manager) {
		EntityPlayerMP player = (EntityPlayerMP)p;
		NBTTagCompound entityData = player.getEntityData();

		if (Constants.STARTING_ITEMS && !entityData.getBoolean("gotDTStartingItems")) {
            entityData.setBoolean("gotDTStartingItems", true);

            entityData.setBoolean("gotNoDTStartingItems", false);
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.doggyCharm));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.commandEmblem));
        }
	}
	
	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) { 
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) { 
		
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
		
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
		
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
		
	}
}
