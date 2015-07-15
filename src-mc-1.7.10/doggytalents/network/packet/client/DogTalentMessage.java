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

public class DogTalentMessage implements IMessage {
	
	public int entityId;
	public String talentId;
	
	public DogTalentMessage() {}
    public DogTalentMessage(int entityId, String talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.talentId = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
		ByteBufUtils.writeUTF8String(buffer, this.talentId);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogTalentMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogTalentMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
	        
	        if(!(target instanceof EntityDog))
	        	return null;
	        
			EntityDog dog = (EntityDog)target;
	        
			dog.talents.setLevel(message.talentId, dog.talents.getLevel(message.talentId) + 1);
			return null;
		}
	}
}
