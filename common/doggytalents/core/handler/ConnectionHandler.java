package doggytalents.core.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class ConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player p, NetHandler netHandler, INetworkManager manager) {
		EntityPlayerMP player = (EntityPlayerMP)p;
		EntityDTDoggy dog = new EntityDTDoggy(player.worldObj);
		dog.setPosition(player.posX, player.posY, player.posZ);
		dog.setOwner(player.getCommandSenderName());
		dog.setTamed(true);
		player.worldObj.spawnEntityInWorld(dog);
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
