package doggytalents.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class DogNameMessage implements IMessage {
	
	public int entityId;
	public String name;
	
	public DogNameMessage() {}
    public DogNameMessage(int entityId, String name) {
        this.entityId = entityId;
        this.name = name;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.name = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
		ByteBufUtils.writeUTF8String(buffer, this.name);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogNameMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogNameMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
			
	        if(!(target instanceof EntityDog))
	        	return null;
	        
			EntityDog dog = (EntityDog)target;
	        
			dog.setDogName(message.name);
			return null;
		}
	}
}
