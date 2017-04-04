package doggytalents.network.packet.client;

import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
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
		Entity target = player.world.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
		if(dog.onGround) {

			double verticalVelocity = 0.0D;
			switch(dog.talents.getLevel("wolfmount")) {
			case 1:
				verticalVelocity = 0.37D;
				break;
			case 2:
				verticalVelocity = 0.47D;
				break;
			case 3:
				verticalVelocity = 0.57D;
				break;
			case 4:
				verticalVelocity = 0.67D;
				break;
			case 5:
				verticalVelocity = 0.87D;
				break;
			default:
				verticalVelocity = 0.0D;
				break;
			}
			
			dog.addVelocity(0D, verticalVelocity, 0D);
			if(dog.isPotionActive(MobEffects.JUMP_BOOST))
				dog.motionY += (double)((float)(dog.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
		}
		else if(dog.isInWater() && dog.talents.getLevel("swimmerdog") > 0) {
			dog.motionY = 0.2F;
		}
	}
}
