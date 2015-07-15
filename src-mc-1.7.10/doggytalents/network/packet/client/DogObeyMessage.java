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

public class DogObeyMessage implements IMessage {
	
	public int entityId;
	public boolean obey;
	
	public DogObeyMessage() {}
    public DogObeyMessage(int entityId, boolean obey) {
        this.entityId = entityId;
        this.obey = obey;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.obey = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeBoolean(this.obey);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogObeyMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogObeyMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
	        if(!(target instanceof EntityDog))
	        	return null;
	        
	        EntityDog dog = (EntityDog)target;
	        
			dog.setWillObeyOthers(message.obey);
			return null;
		}
	}
}
