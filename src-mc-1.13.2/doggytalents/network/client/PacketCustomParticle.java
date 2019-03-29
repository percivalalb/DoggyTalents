package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCustomParticle {
	
    private BlockPos pos;
    private float posX, posY, posZ;
    private int numberOfParticles;
    private float particleSpeed;
	
	public PacketCustomParticle(BlockPos pos, double posX, double posY, double posZ, int numberOfParticles, float particleSpeed) {
        this.pos = pos;
        this.posX = (float)posX;
        this.posY = (float)posY;
        this.posZ = (float)posZ;
        this.numberOfParticles = numberOfParticles;
        this.particleSpeed = particleSpeed;
    }
    
	public static void encode(PacketCustomParticle msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeInt(msg.numberOfParticles);
		buf.writeFloat(msg.posX);
		buf.writeFloat(msg.posY);
		buf.writeFloat(msg.posZ);
		buf.writeFloat(msg.particleSpeed);
	}
	
	public static PacketCustomParticle decode(PacketBuffer buf) {
		BlockPos pos = buf.readBlockPos();
        int numberOfParticles = buf.readInt();
        float posX = buf.readFloat();
        float posY = buf.readFloat();
        float posZ = buf.readFloat();
        float particleSpeed = buf.readFloat();
		return new PacketCustomParticle(pos, posX, posY, posZ, numberOfParticles, particleSpeed);
	}
	
	
	public static class Handler {
        public static void handle(final PacketCustomParticle message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	EntityPlayer player = DoggyTalentsMod.PROXY.getPlayerEntity();
            	DoggyTalentsMod.PROXY.spawnCustomParticle(player, message.pos, player.getRNG(), message.posX, message.posY, message.posZ, message.numberOfParticles, message.particleSpeed);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
