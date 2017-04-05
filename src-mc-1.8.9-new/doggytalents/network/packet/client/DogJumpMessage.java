package doggytalents.network.packet.client;

import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;

public class DogJumpMessage extends AbstractServerMessage {
	
	public int entityId;
	
	public DogJumpMessage() {}
    public DogJumpMessage(int entityId) {
        this.entityId = entityId;
    }
    
	@Override
	public void read(PacketBuffer buffer) {
		this.entityId = buffer.readInt();
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
   
        EntityDog dog = (EntityDog)target;
        if(dog.onGround) {

        	double verticalVelocity = 0.28D + 0.09D * dog.talents.getLevel("wolfmount");
        	if(dog.talents.getLevel("wolfmount") == 5) verticalVelocity += 0.1D;
        	
			dog.addVelocity(0D, verticalVelocity, 0D);
			if(dog.isPotionActive(Potion.jump))
				dog.motionY += (double)((float)(dog.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		}
		else if(dog.isInWater() && dog.talents.getLevel("swimmerdog") > 0) {
			dog.motionY = 0.2F;
		}
	}
}
