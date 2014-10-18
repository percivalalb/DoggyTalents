package doggytalents.network;

import doggytalents.network.packet.PacketCommand;
import doggytalents.network.packet.PacketDogBedUpdate;
import doggytalents.network.packet.PacketDogJump;
import doggytalents.network.packet.PacketDoggyMode;
import doggytalents.network.packet.PacketDoggyName;
import doggytalents.network.packet.PacketDoggyTexture;
import doggytalents.network.packet.PacketObeyOthers;
import doggytalents.network.packet.PacketTalentToServer;

/**
 * @author ProPercivalalb
 */
public enum PacketType {
	
	TALENT_TO_SERVER(PacketTalentToServer.class),
	DOGGY_TEXTURE(PacketDoggyTexture.class),
	OBEY_OTHERS(PacketObeyOthers.class),
	DOGGY_MODE(PacketDoggyMode.class),
	DOGGY_NAME(PacketDoggyName.class),
	DOG_BED_UPDATE(PacketDogBedUpdate.class),
	DOG_COMMAND(PacketCommand.class),
	DOG_JUMP(PacketDogJump.class);
	
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