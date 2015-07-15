package doggytalents.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class DogJumpMessage implements IMessage {
	
	public int entityId;
	
	public DogJumpMessage() {}
    public DogJumpMessage(int entityId) {
        this.entityId = entityId;
    }
    
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityId = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}
	
	public static class Handler extends AbstractServerMessageHandler<DogJumpMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage handleServerMessage(EntityPlayer player, DogJumpMessage message, MessageContext ctx) {
			Entity target = player.worldObj.getEntityByID(message.entityId);
	        if(!(target instanceof EntityDog))
	        	return null;
	        
	        EntityDog dog = (EntityDog)target;
			if(dog.onGround) {
				
				dog.motionY = 2F * dog.talents.getLevel("wolfmount") * 0.1F;
				if(dog.isPotionActive(Potion.jump))
					dog.motionY += (double)((float)(dog.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
				dog.isAirBorne = true;
			}
			else if(dog.isInWater() && dog.talents.getLevel("swimmerdog") > 0) {
				dog.motionY = 0.2F;
			}
			return null;
		}
	}
}
