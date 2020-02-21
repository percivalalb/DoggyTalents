package doggytalents.network.client;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class DogModeMessage extends AbstractServerMessage {
    
    public int entityId;
    public EnumMode mode;
    
    public DogModeMessage() {}
    public DogModeMessage(int entityId, EnumMode modeIn) {
        this.entityId = entityId;
        this.mode = modeIn;
    }
    
    @Override
    public void read(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        this.mode = EnumMode.byIndex(buffer.readInt());
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.mode.getIndex());
    }
    
    @Override
    public void process(EntityPlayer player, Side side) {
        Entity target = player.world.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
            return;
        
        EntityDog dog = (EntityDog)target;
        
        if(!dog.canInteract(player))
            return;
        
        dog.setMode(this.mode);
    }
}
