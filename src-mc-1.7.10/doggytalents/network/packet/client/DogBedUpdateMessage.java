package doggytalents.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.network.AbstractMessage.AbstractClientMessage;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

public class DogBedUpdateMessage extends AbstractClientMessage {
	
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
	public void read(PacketBuffer buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.casingId = ByteBufUtils.readUTF8String(buffer);
		this.bedingId = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		ByteBufUtils.writeUTF8String(buffer, this.casingId);
		ByteBufUtils.writeUTF8String(buffer, this.bedingId);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		TileEntity target = player.worldObj.getTileEntity(this.x, this.y, this.z);
		
		if(!(target instanceof TileEntityDogBed))
			return;
		
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		dogBed.setCasingId(this.casingId);
		dogBed.setBeddingId(this.bedingId);
	}
}
