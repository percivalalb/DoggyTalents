package doggytalents.common.network;

import doggytalents.DoggyTalents2;
import doggytalents.common.network.packet.*;
import doggytalents.common.network.packet.ParticlePackets.CritEmitterPacket;
import doggytalents.common.network.packet.data.*;
import doggytalents.common.network.packet.data.ParticleData.CritEmitterData;
import net.minecraftforge.network.PacketDistributor;

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
        registerPacket(new OpenDogScreenPacket(), OpenDogScreenData.class);
        registerPacket(new DogInventoryPagePacket(), DogInventoryPageData.class);
        registerPacket(new DogTexturePacket(), DogTextureData.class);
        registerPacket(new CritEmitterPacket(), CritEmitterData.class); 
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        DoggyTalents2.HANDLER.send(target, message);
    }

    public static <D> void registerPacket(IPacket<D> packet, Class<D> dataClass) {
        DoggyTalents2.HANDLER.registerMessage(PacketHandler.disc++, dataClass, packet::encode, packet::decode, packet::handle);
    }
}
