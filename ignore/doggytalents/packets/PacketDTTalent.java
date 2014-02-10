package doggytalents.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.EnumSkills;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketDTTalent implements IPacket {

	@Override
	public void handle(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
		try
        {
			DataInputStream var1 = new DataInputStream(new ByteArrayInputStream(packet.data));
            int var2 = var1.readInt();
            int var3 = var1.readInt();
            Entity target = player.worldObj.getEntityByID(var3);
            
            if(!(target instanceof EntityDTDoggy))
            	return;
            
			EntityDTDoggy dog = (EntityDTDoggy)target;
            
			switch(var2) {
			case 0:
				dog.talents.increaseTalentLevel(EnumSkills.BLACKPELT, 1);
				break;
			case 1:
				dog.talents.increaseTalentLevel(EnumSkills.GUARDDOG, 1);
				break;
			case 2:
				dog.talents.increaseTalentLevel(EnumSkills.HUNTERDOG, 1);
				break;
			case 3:
				dog.talents.increaseTalentLevel(EnumSkills.HELLHOUND, 1);
				break;
			case 4:
				dog.talents.increaseTalentLevel(EnumSkills.WOLFMOUNT, 1);
				break;
			case 5:
				dog.talents.increaseTalentLevel(EnumSkills.PACKPUPPY, 1);
				break;
			case 6:
				dog.talents.increaseTalentLevel(EnumSkills.PILLOWPAW, 1);
				break;
			case 7:
				dog.talents.increaseTalentLevel(EnumSkills.QUICKHEALER, 1);
				break;
			case 8:
				dog.talents.increaseTalentLevel(EnumSkills.CREEPERSWEEPER, 1);
				break;
			case 9:
				dog.talents.increaseTalentLevel(EnumSkills.DOGGYDASH, 1);
				break;
			case 10:
				dog.talents.increaseTalentLevel(EnumSkills.FISHERDOG, 1);
				break;
			case 11:
				dog.talents.increaseTalentLevel(EnumSkills.HAPPYEATER, 1);
				break;
			case 12:
				dog.talents.increaseTalentLevel(EnumSkills.BEDFINDER, 1);
				break;
			case 13:
				dog.talents.increaseTalentLevel(EnumSkills.PESTFIGHTER, 1);
				break;
			case 14:
				dog.talents.increaseTalentLevel(EnumSkills.POSIONFANG, 1);
				break;
			case 15:
				dog.talents.increaseTalentLevel(EnumSkills.SHEPHERDOG, 1);
				break;
			case 16:
				dog.talents.increaseTalentLevel(EnumSkills.RESCUEDOG, 1);
				break;
			case 17:
				dog.talents.increaseTalentLevel(EnumSkills.PUPPYEYES, 1);
				break;
			}
        }
        catch (Exception var9)
        {
            var9.printStackTrace();
        }
	}
}
