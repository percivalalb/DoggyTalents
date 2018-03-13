package doggytalents.network.packet.client;

import java.util.Random;

import doggytalents.DoggyTalents;
import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class CustomParticleMessage extends AbstractClientMessage {
	
    private World world;
    private BlockPos pos;
    private float posX, posY, posZ;
    private int numberOfParticles;
    private float particleSpeed;
	
	public CustomParticleMessage() {}
	public CustomParticleMessage(World world, BlockPos pos, double posX, double posY, double posZ, int numberOfParticles, float particleSpeed) {
        this.world = world;
        this.pos = pos;
        this.posX = (float)posX;
        this.posY = (float)posY;
        this.posZ = (float)posZ;
        this.numberOfParticles = numberOfParticles;
        this.particleSpeed = particleSpeed;
    }
    
	@Override
	public void read(PacketBuffer buffer) {
        this.pos = buffer.readBlockPos();
        this.numberOfParticles = buffer.readInt();
        this.posX = buffer.readFloat();
        this.posY = buffer.readFloat();
        this.posZ = buffer.readFloat();
        this.particleSpeed = buffer.readFloat();
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
		buffer.writeInt(this.numberOfParticles);
		buffer.writeFloat(this.posX);
		buffer.writeFloat(this.posY);
		buffer.writeFloat(this.posZ);
		buffer.writeFloat(this.particleSpeed);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		DoggyTalents.PROXY.spawnCustomParticle(player, this.pos, player.getRNG(), this.posX, this.posY, this.posZ, this.numberOfParticles, this.particleSpeed);
	}
}
