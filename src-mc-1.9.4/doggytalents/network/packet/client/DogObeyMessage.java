package doggytalents.network.packet.client;

import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class DogObeyMessage extends AbstractServerMessage {
	
	public int entityId;
	public boolean obey;
	
	public DogObeyMessage() {}
    public DogObeyMessage(int entityId, boolean obey) {
        this.entityId = entityId;
        this.obey = obey;
    }
    
	@Override
	public void read(PacketBuffer buffer) {
		this.entityId = buffer.readInt();
		this.obey = buffer.readBoolean();
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeBoolean(this.obey);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
        
		dog.setWillObeyOthers(this.obey);
	}
}
