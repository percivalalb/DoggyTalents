package doggytalents.common.network.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import doggytalents.DoggyTalents2;
import doggytalents.client.DogTextureLoaderClient;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.texture.DogTextureLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendSkinPacket {

    public Integer entityId;
    public byte[] image;

    public SendSkinPacket(int entityId, InputStream imageIn) throws IOException {
        this(entityId, IOUtils.toByteArray(imageIn));
    }

    public SendSkinPacket(int entityId, byte[] imageIn) {
        this.entityId = entityId;
        this.image = imageIn;
    }

    public static void encode(SendSkinPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.image.length);
        buf.writeBytes(msg.image);
    }

    public static SendSkinPacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        byte[] targetArray = new byte[buf.readInt()];
        buf.readBytes(targetArray);
        return new SendSkinPacket(entityId, targetArray);
    }

    public static class Handler {
        public static void handle(final SendSkinPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                LogicalSide side = ctx.get().getDirection().getReceptionSide();
                if (side.isClient()) {
                    DoggyTalents2.LOGGER.debug("Client: Received dog texture to save and load");
                    String hash = DogTextureLoaderClient.saveTextureAndLoad(DogTextureLoaderClient.getClientFolder(), msg.image);
                    DogTextureLoaderClient.SKIN_REQUEST_MAP.put(hash, DogTextureLoaderClient.SkinRequest.RECEIVED);
                } else if (side.isServer()) {
                    Entity target = ctx.get().getSender().world.getEntityByID(msg.entityId);
                    if(!(target instanceof DogEntity)) {
                        return;
                    }

                    DogEntity dog = (DogEntity) target;
                    if(!dog.canInteract(ctx.get().getSender())) {
                        return;
                    }

                    try {
                        if (ctx.get().getSender().getServer().isDedicatedServer()) {

                            // Sanitise the data
                            ByteArrayInputStream bis = new ByteArrayInputStream(msg.image);
                            BufferedImage bImage2 = ImageIO.read(bis);

                            DogTextureLoader.saveTexture(DogTextureLoader.getServerFolder(), IOUtils.toByteArray(bis));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String hash = DogTextureLoader.getHash(msg.image);
                    dog.setSkinHash(hash);
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}