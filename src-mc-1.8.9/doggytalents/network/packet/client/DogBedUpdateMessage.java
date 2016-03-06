package doggytalents.network.packet.client;

import doggytalents.network.AbstractMessage.AbstractClientMessage;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class DogBedUpdateMessage extends AbstractClientMessage {
	
	public BlockPos pos;
	public String casingId, bedingId;
	
	public DogBedUpdateMessage() {}
    public DogBedUpdateMessage(BlockPos pos, String casingId, String bedingId) {
		this.pos = pos;
		this.casingId = casingId;
		this.bedingId = bedingId;
	}
    
	@Override
	public void read(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		this.casingId = ByteBufUtils.readUTF8String(buffer);
		this.bedingId = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
		ByteBufUtils.writeUTF8String(buffer, this.casingId);
		ByteBufUtils.writeUTF8String(buffer, this.bedingId);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		TileEntity target = player.worldObj.getTileEntity(this.pos);
		
		if(!(target instanceof TileEntityDogBed))
			return;
		
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		dogBed.setCasingId(this.casingId);
		dogBed.setBeddingId(this.bedingId);
	}
}
