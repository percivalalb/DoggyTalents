package doggytalents.network.packet.client;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

public class DogTalentMessage extends AbstractServerMessage {
	
	public int entityId;
	public ResourceLocation talentId;
	
	public DogTalentMessage() {}
    public DogTalentMessage(int entityId, ResourceLocation talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }
    
	@Override
	public void read(PacketBuffer buf) {
		this.entityId = buf.readInt();
		this.talentId = buf.readResourceLocation();
	}

	@Override
	public void write(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeResourceLocation(this.talentId);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		Entity target = player.world.getEntityByID(this.entityId);
        
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
		Talent talent = DoggyTalentsAPI.TALENTS.getValue(this.talentId);
		int level = dog.TALENTS.getLevel(talent);
		
		if(level < talent.getHighestLevel(dog) && dog.spendablePoints() >= talent.getCost(dog, level + 1))
			dog.TALENTS.setLevel(talent, level + 1);
	}
}
