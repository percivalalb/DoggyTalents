package doggytalents.network;

import doggytalents.network.packet.PacketCommand;
import doggytalents.network.packet.PacketDogBedUpdate;
import doggytalents.network.packet.PacketDogJump;
import doggytalents.network.packet.PacketDogMode;
import doggytalents.network.packet.PacketDogName;
import doggytalents.network.packet.PacketDogObey;
import doggytalents.network.packet.PacketDogTalent;
import doggytalents.network.packet.PacketDogTexture;

/**
 * @author ProPercivalalb
 */
public enum PacketType {
	
	DOG_BED_UPDATE(PacketDogBedUpdate.class),
	DOG_NAME(PacketDogName.class),
	DOG_TALENT(PacketDogTalent.class),
	DOG_TEXTURE(PacketDogTexture.class),
	DOG_OBEY(PacketDogObey.class),
	DOG_JUMP(PacketDogJump.class),
	DOG_MODE(PacketDogMode.class),
	COMMAND(PacketCommand.class);
	
    public Class<? extends IPacket> packetClass;

    PacketType(Class<? extends IPacket> packetClass) {
        this.packetClass = packetClass;
    }
	public static byte getIdFromClass(Class<? extends IPacket> packetClass) {
		for(PacketType type : values())
			if(type.packetClass == packetClass)
				return (byte)type.ordinal();
		return -1;
	}
}