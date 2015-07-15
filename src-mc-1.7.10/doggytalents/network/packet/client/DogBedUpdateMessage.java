package doggytalents.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.network.AbstractClientMessageHandler;
import doggytalents.tileentity.TileEntityDogBed;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class DogBedUpdateMessage implements IMessage {
	
	public int x, y, z;
	public String casingId, bedingId;
	
	public DogBedUpdateMessage() {}
    public DogBedUpdateMessage(int x, int y, int z, String casingId, String bedingId) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.casingId = casingId;
		this.bedingId = bedingId;
	}
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.casingId = ByteBufUtils.readUTF8String(buffer);
		this.bedingId = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		ByteBufUtils.writeUTF8String(buffer, this.casingId);
		ByteBufUtils.writeUTF8String(buffer, this.bedingId);
	}
	
	public static class Handler extends AbstractClientMessageHandler<DogBedUpdateMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleClientMessage(EntityPlayer player, DogBedUpdateMessage message, MessageContext ctx) {
			TileEntity target = player.worldObj.getTileEntity(message.x, message.y, message.z);
			
			if(!(target instanceof TileEntityDogBed))
				return null;
			
			TileEntityDogBed dogBed = (TileEntityDogBed)target;
			dogBed.setCasingId(message.casingId);
			dogBed.setBeddingId(message.bedingId);
			return null;
		}
	}
}
