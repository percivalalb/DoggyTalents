package doggytalents.network;

import doggytalents.DoggyTalentsMod;
import doggytalents.network.client.PacketDogMode;
import doggytalents.network.client.PacketDogName;
import doggytalents.network.client.PacketDogObey;
import doggytalents.network.client.PacketDogTalent;
import doggytalents.network.client.PacketDogTexture;
import doggytalents.network.client.PacketFriendlyFire;
import net.minecraftforge.fml.network.PacketDistributor;

public final class PacketHandler
{
    
    public static void register() {
        int disc = 0;

        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketDogMode.class, PacketDogMode::encode, PacketDogMode::decode, PacketDogMode.Handler::handle);
        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketDogName.class, PacketDogName::encode, PacketDogName::decode, PacketDogName.Handler::handle);
        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketDogObey.class, PacketDogObey::encode, PacketDogObey::decode, PacketDogObey.Handler::handle);
        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketDogTalent.class, PacketDogTalent::encode, PacketDogTalent::decode, PacketDogTalent.Handler::handle);
        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketDogTexture.class, PacketDogTexture::encode, PacketDogTexture::decode, PacketDogTexture.Handler::handle);
        DoggyTalentsMod.HANDLER.registerMessage(disc++, PacketFriendlyFire.class, PacketFriendlyFire::encode, PacketFriendlyFire::decode, PacketFriendlyFire.Handler::handle);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        DoggyTalentsMod.HANDLER.send(target, message);
    }
}