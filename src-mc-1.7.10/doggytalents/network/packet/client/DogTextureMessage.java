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

public class DogTextureMessage implements IMessage {
	
	public int entityId, doggyTexture;
	
	public DogTextureMessage() {}
    public DogTextureMessage(int entityId, int doggyTexture) {
        this.entityId = entityId;
        this.doggyTexture = doggyTexture;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.doggyTexture = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeInt(this.doggyTexture);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogTextureMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogTextureMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
	        if(!(target instanceof EntityDog))
	        	return null;
	        
	        EntityDog dog = (EntityDog)target;
	        
			dog.setTameSkin(message.doggyTexture);
			return null;
		}
	}
}
