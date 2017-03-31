package doggytalents.network.packet.client;

import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class DogTextureMessage extends AbstractServerMessage {
	
	public int entityId, doggyTexture;
	
	public DogTextureMessage() {}
    public DogTextureMessage(int entityId, int doggyTexture) {
        this.entityId = entityId;
        this.doggyTexture = doggyTexture;
    }
    
	@Override
	public void read(PacketBuffer buffer) {
		this.entityId = buffer.readInt();
		this.doggyTexture = buffer.readInt();
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeInt(this.doggyTexture);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
        
		dog.setTameSkin(this.doggyTexture);
	}
}
