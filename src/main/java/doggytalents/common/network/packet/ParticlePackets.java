package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.network.IPacket;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.ParticleData.CritEmitterData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class ParticlePackets {
    public static class CritEmitterPacket implements IPacket<CritEmitterData> {
        
        @Override
        public void encode(CritEmitterData data, PacketBuffer buf) {
            buf.writeInt(data.targetId);
        }
    
        @Override
        public CritEmitterData decode(PacketBuffer buf) {
            return new CritEmitterData(buf.readInt());
        }
    
        @Override
        public void handle(CritEmitterData data, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                
                if (ctx.get().getDirection().getReceptionSide().isClient()) { 
                    Minecraft mc = Minecraft.getInstance();
                    Entity e = mc.level.getEntity(data.targetId);
                    if (e != null) {
                        Minecraft.getInstance().particleEngine.createTrackingEmitter(e, ParticleTypes.CRIT);
                    }
                }
                
            });
    
            ctx.get().setPacketHandled(true);
        }

        public static void sendCritEmitterPacketToNearClients(Entity e) {
            final int RADIUS = 64;
            TargetPoint tarp = new TargetPoint( e.getX(), e.getY(), e.getZ(), RADIUS , e.level.dimension());
            PacketHandler.send(PacketDistributor.NEAR.with(() -> tarp ), new CritEmitterData(e.getId()));
        }
    
    }
}
