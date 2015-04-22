package doggytalents.network.packet;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import doggytalents.entity.EntityDog;
import doggytalents.network.IPacket;

/**
 * @author ProPercivalalb
 */
public class PacketDogTalent extends IPacket {

	public int entityId;
	public String talentId;
	
	public PacketDogTalent() {}
	public PacketDogTalent(int entityId, String talentId) {
		this();
		this.entityId = entityId;
		this.talentId = talentId;
	}

	@Override
	public void read(PacketBuffer packetbuffer) throws IOException {
		this.entityId = packetbuffer.readInt();
		this.talentId = packetbuffer.readStringFromBuffer(MAX_STRING_LENGTH);
	}

	@Override
	public void write(PacketBuffer packetbuffer) throws IOException {
		packetbuffer.writeInt(this.entityId);
		packetbuffer.writeString(this.talentId);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        
        if(!(target instanceof EntityDog))
        	return;
        
		EntityDog dog = (EntityDog)target;
        
		dog.talents.setLevel(this.talentId, dog.talents.getLevel(this.talentId) + 1);
	}

}
