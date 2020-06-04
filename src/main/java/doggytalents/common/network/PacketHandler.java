package doggytalents.common.network;

import doggytalents.DoggyTalents2;
import doggytalents.common.network.packet.DogModePacket;
import doggytalents.common.network.packet.DogNamePacket;
import doggytalents.common.network.packet.DogObeyPacket;
import doggytalents.common.network.packet.DogTalentPacket;
import doggytalents.common.network.packet.FriendlyFirePacket;
import doggytalents.common.network.packet.RequestSkinPacket;
import doggytalents.common.network.packet.SendSkinPacket;
import net.minecraftforge.fml.network.PacketDistributor;

public final class PacketHandler {

    public static void init() {
        int disc = 0;

        DoggyTalents2.HANDLER.registerMessage(disc++, DogModePacket.class, DogModePacket::encode, DogModePacket::decode, DogModePacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, DogNamePacket.class, DogNamePacket::encode, DogNamePacket::decode, DogNamePacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, DogObeyPacket.class, DogObeyPacket::encode, DogObeyPacket::decode, DogObeyPacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, DogTalentPacket.class, DogTalentPacket::encode, DogTalentPacket::decode, DogTalentPacket.Handler::handle);
//        DoggyTalents2.HANDLER.registerMessage(disc++, DogTexturePacket.class, DogTexturePacket::encode, DogTexturePacket::decode, DogTexturePacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, FriendlyFirePacket.class, FriendlyFirePacket::encode, FriendlyFirePacket::decode, FriendlyFirePacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, SendSkinPacket.class, SendSkinPacket::encode, SendSkinPacket::decode, SendSkinPacket.Handler::handle);
        DoggyTalents2.HANDLER.registerMessage(disc++, RequestSkinPacket.class, RequestSkinPacket::encode, RequestSkinPacket::decode, RequestSkinPacket.Handler::handle);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        DoggyTalents2.HANDLER.send(target, message);
    }
}