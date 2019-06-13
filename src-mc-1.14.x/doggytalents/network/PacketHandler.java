package doggytalents.network;

import doggytalents.lib.Reference;
import doggytalents.network.client.PacketCustomParticle;
import doggytalents.network.client.PacketDogMode;
import doggytalents.network.client.PacketDogName;
import doggytalents.network.client.PacketDogObey;
import doggytalents.network.client.PacketDogTalent;
import doggytalents.network.client.PacketDogTexture;
import doggytalents.network.client.PacketFriendlyFire;
import doggytalents.network.client.PacketJump;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(2);

    
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    
    public static void register()  {
        int disc = 0;

        HANDLER.registerMessage(disc++, PacketDogMode.class, PacketDogMode::encode, PacketDogMode::decode, PacketDogMode.Handler::handle);
        HANDLER.registerMessage(disc++, PacketDogName.class, PacketDogName::encode, PacketDogName::decode, PacketDogName.Handler::handle);
        HANDLER.registerMessage(disc++, PacketDogObey.class, PacketDogObey::encode, PacketDogObey::decode, PacketDogObey.Handler::handle);
        HANDLER.registerMessage(disc++, PacketDogTalent.class, PacketDogTalent::encode, PacketDogTalent::decode, PacketDogTalent.Handler::handle);
        HANDLER.registerMessage(disc++, PacketDogTexture.class, PacketDogTexture::encode, PacketDogTexture::decode, PacketDogTexture.Handler::handle);
        HANDLER.registerMessage(disc++, PacketFriendlyFire.class, PacketFriendlyFire::encode, PacketFriendlyFire::decode, PacketFriendlyFire.Handler::handle);
        HANDLER.registerMessage(disc++, PacketJump.class, PacketJump::encode, PacketJump::decode, PacketJump.Handler::handle);
        HANDLER.registerMessage(disc++, PacketCustomParticle.class, PacketCustomParticle::encode, PacketCustomParticle::decode, PacketCustomParticle.Handler::handle);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        HANDLER.send(target, message);
    }
}