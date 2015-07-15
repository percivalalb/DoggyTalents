package doggytalents.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class DogModeMessage implements IMessage {
	
	public int entityId, doggyMode;
	
	public DogModeMessage() {}
    public DogModeMessage(int entityId, int dogMode) {
        this.entityId = entityId;
        this.doggyMode = dogMode;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.doggyMode = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeInt(this.doggyMode);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogModeMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogModeMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
	        if(!(target instanceof EntityDog))
	        	return null;
	        
	        EntityDog dog = (EntityDog)target;
	        
			dog.mode.setMode(message.doggyMode);
			return null;
		}
	}
}
