package doggytalents.common.network;

import doggytalents.DoggyTalents2;
import doggytalents.common.network.packet.DogModePacket;
import doggytalents.common.network.packet.DogNamePacket;
import doggytalents.common.network.packet.DogObeyPacket;
import doggytalents.common.network.packet.DogTalentPacket;
import doggytalents.common.network.packet.FriendlyFirePacket;
import doggytalents.common.network.packet.RequestSkinPacket;
import doggytalents.common.network.packet.SendSkinPacket;
import doggytalents.common.network.packet.data.DogModeData;
import doggytalents.common.network.packet.data.DogNameData;
import doggytalents.common.network.packet.data.DogObeyData;
import doggytalents.common.network.packet.data.DogTalentData;
import doggytalents.common.network.packet.data.FriendlyFireData;
import doggytalents.common.network.packet.data.RequestSkinData;
import doggytalents.common.network.packet.data.SendSkinData;
import net.minecraftforge.fml.network.PacketDistributor;

public final class PacketHandler {

    private static int disc = 0;

    public static void init() {
        registerPacket(new DogModePacket(), DogModeData.class);
        registerPacket(new DogNamePacket(), DogNameData.class);
        registerPacket(new DogObeyPacket(), DogObeyData.class);
        registerPacket(new DogTalentPacket(), DogTalentData.class);
        //registerPacket(new DogTexturePacket(), DogTextureData.class);
        registerPacket(new FriendlyFirePacket(), FriendlyFireData.class);
        registerPacket(new SendSkinPacket(), SendSkinData.class);
        registerPacket(new RequestSkinPacket(), RequestSkinData.class);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        DoggyTalents2.HANDLER.send(target, message);
    }

    public static <D> void registerPacket(IPacket<D> packet, Class<D> dataClass) {
        DoggyTalents2.HANDLER.registerMessage(PacketHandler.disc++, dataClass, packet::encode, packet::decode, packet::handle);
    }
}
