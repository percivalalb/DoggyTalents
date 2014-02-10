package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import doggytalents.network.PacketTypeHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetworkManager;

public class PacketTalentUpdate extends PacketDT {

	public int entityId, talentId;
	
	public PacketTalentUpdate() {
		super(PacketTypeHandler.TALENT_UPDATE, false);
	}
	
	public PacketTalentUpdate(int entityId, int talentId) {
		this();
		this.entityId = entityId;
		this.talentId = talentId;
	}
	
	public PacketTalentUpdate(Entity entity, int talentId) {
		this(entity.entityId, talentId);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.talentId = data.readInt();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeInt(talentId);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		
		//DoggyTalentsMod.proxy
	}

}
