package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;
import doggytalents.network.PacketTypeHandler;

/**
 * @author ProPercivalalb
 */
public class PacketTalentToServer extends DTPacket {

	public int entityId, talentId;
	
	public PacketTalentToServer() {
		super(PacketTypeHandler.TALENT_TO_SERVER, false);
	}
	
	public PacketTalentToServer(int entityId, int talentId) {
		this();
		this.entityId = entityId;
		this.talentId = talentId;
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
		Entity target = player.worldObj.getEntityByID(entityId);
        
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		switch(talentId) {
		case 0:
			dog.talents.increaseTalentLevel(EnumTalents.BLACKPELT, 1);
			break;
		case 1:
			dog.talents.increaseTalentLevel(EnumTalents.GUARDDOG, 1);
			break;
		case 2:
			dog.talents.increaseTalentLevel(EnumTalents.HUNTERDOG, 1);
			break;
		case 3:
			dog.talents.increaseTalentLevel(EnumTalents.HELLHOUND, 1);
			break;
		case 4:
			dog.talents.increaseTalentLevel(EnumTalents.WOLFMOUNT, 1);
			break;
		case 5:
			dog.talents.increaseTalentLevel(EnumTalents.PACKPUPPY, 1);
			break;
		case 6:
			dog.talents.increaseTalentLevel(EnumTalents.PILLOWPAW, 1);
			break;
		case 7:
			dog.talents.increaseTalentLevel(EnumTalents.QUICKHEALER, 1);
			break;
		case 8:
			dog.talents.increaseTalentLevel(EnumTalents.CREEPERSWEEPER, 1);
			break;
		case 9:
			dog.talents.increaseTalentLevel(EnumTalents.DOGGYDASH, 1);
			break;
		case 10:
			dog.talents.increaseTalentLevel(EnumTalents.FISHERDOG, 1);
			break;
		case 11:
			dog.talents.increaseTalentLevel(EnumTalents.HAPPYEATER, 1);
			break;
		case 12:
			dog.talents.increaseTalentLevel(EnumTalents.BEDFINDER, 1);
			break;
		case 13:
			dog.talents.increaseTalentLevel(EnumTalents.PESTFIGHTER, 1);
			break;
		case 14:
			dog.talents.increaseTalentLevel(EnumTalents.POSIONFANG, 1);
			break;
		case 15:
			dog.talents.increaseTalentLevel(EnumTalents.SHEPHERDDOG, 1);
			break;
		case 16:
			dog.talents.increaseTalentLevel(EnumTalents.RESCUEDOG, 1);
			break;
		case 17:
			dog.talents.increaseTalentLevel(EnumTalents.PUPPYEYES, 1);
			break;
		}
	}

}
