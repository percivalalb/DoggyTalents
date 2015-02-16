package doggytalents.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import doggytalents.helper.LogHelper;
import doggytalents.network.IPacket;
import doggytalents.tileentity.TileEntityDogBed;

/**
 * @author ProPercivalalb
 */
public class PacketDogBedUpdate extends IPacket {

	public BlockPos pos;
	public String casingId, bedingId;
	
	public PacketDogBedUpdate() {}
	public PacketDogBedUpdate(BlockPos pos, String casingId, String bedingId) {
		this();
		LogHelper.info("create");
		this.pos = pos;
		this.casingId = casingId;
		this.bedingId = bedingId;
	}

	@Override
	public void read(PacketBuffer packetbuffer) throws IOException {
		this.pos = packetbuffer.readBlockPos();
		this.casingId = packetbuffer.readStringFromBuffer(MAX_STRING_LENGTH);
		this.bedingId = packetbuffer.readStringFromBuffer(MAX_STRING_LENGTH);
	}

	@Override
	public void write(PacketBuffer packetbuffer) throws IOException {
		packetbuffer.writeBlockPos(this.pos);
		packetbuffer.writeString(this.casingId);
		packetbuffer.writeString(this.bedingId);
	}

	@Override
	public void execute(EntityPlayer player) {
		TileEntity target = player.worldObj.getTileEntity(this.pos);
		LogHelper.info(this.pos.toString());
		if(!(target instanceof TileEntityDogBed))
			return;
		
		LogHelper.info("isBed");
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		LogHelper.info(this.casingId + " " + this.bedingId);
		dogBed.setCasingId(this.casingId);
		dogBed.setBeddingId(this.bedingId);
		dogBed.getWorld().markBlockRangeForRenderUpdate(dogBed.getPos(), dogBed.getPos());
	}

}
