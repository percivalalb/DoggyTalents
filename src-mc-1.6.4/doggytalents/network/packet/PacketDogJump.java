package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import doggytalents.entity.EntityDog;
import doggytalents.network.IPacket;

/**
 * @author ProPercivalalb
 */
public class PacketDogJump extends IPacket {

	public int entityId;
	
	public PacketDogJump() {}
	public PacketDogJump(int entityId) {
		this();
		this.entityId = entityId;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(this.entityId);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
		if(dog.onGround) {
			
			dog.motionY = 2F * dog.talents.getLevel("wolfmount") * 0.1F;
			if(dog.isPotionActive(Potion.jump))
				dog.motionY += (double)((float)(dog.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
			dog.isAirBorne = true;
		}
	}

}
