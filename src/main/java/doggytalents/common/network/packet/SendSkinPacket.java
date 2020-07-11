package doggytalents.common.network.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import doggytalents.DoggyTalents2;
import doggytalents.client.DogTextureManager;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.texture.DogTextureServer;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.SendSkinData;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SendSkinPacket implements IPacket<SendSkinData> {

    @Override
    public void encode(SendSkinData data, PacketBuffer buf) {
        buf.writeInt(data.entityId);
        buf.writeInt(data.image.length);
        buf.writeBytes(data.image);
    }

    @Override
    public SendSkinData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        byte[] targetArray = new byte[buf.readInt()];
        buf.readBytes(targetArray);
        return new SendSkinData(entityId, targetArray);
    }

    @Override
    public void handle(SendSkinData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LogicalSide side = ctx.get().getDirection().getReceptionSide();
            if (side.isClient()) {
                DoggyTalents2.LOGGER.debug("Client: Received dog texture to save and load");
                String hash = "";
                try {
                    hash = DogTextureManager.INSTANCE.saveTextureAndLoad(DogTextureManager.INSTANCE.getClientFolder(), data.image);

                    DogTextureManager.INSTANCE.setRequestHandled(hash);
                } catch (IOException e) {
                    DoggyTalents2.LOGGER.error("Dog skin failed to load");
                    DogTextureManager.INSTANCE.setRequestFailed(hash);
                }
            } else if (side.isServer()) {
                Entity target = ctx.get().getSender().world.getEntityByID(data.entityId);
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
                        ByteArrayInputStream bis = new ByteArrayInputStream(data.image);
                        BufferedImage bImage2 = ImageIO.read(bis);


                        DogTextureServer.INSTANCE.saveTexture(DogTextureServer.INSTANCE.getServerFolder(), IOUtils.toByteArray(bis));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String hash = DogTextureServer.INSTANCE.getHash(data.image);
                dog.setSkinHash(hash);
            }
        });

        ctx.get().setPacketHandled(true);

    }
}
