package doggytalents.network.packet.client;

import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
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
        
    }
}
