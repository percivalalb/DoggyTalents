package doggytalents.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

import cpw.mods.fml.common.network.Player;
import doggytalents.network.PacketTypeHandler;


public abstract class DTPacket {

    public PacketTypeHandler packetType;
    public boolean isChunkDataPacket;

    public DTPacket(PacketTypeHandler packetType, boolean isChunkDataPacket) {

        this.packetType = packetType;
        this.isChunkDataPacket = isChunkDataPacket;
    }

    public final byte[] populate() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(packetType.ordinal());
            this.writeData(dos);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return bos.toByteArray();
    }

    public final void readPopulate(DataInputStream data) {
        try {
            this.readData(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void readData(DataInputStream data) throws IOException;
    public abstract void writeData(DataOutputStream dos) throws IOException;
    public abstract void execute(INetworkManager network, EntityPlayer player);
}