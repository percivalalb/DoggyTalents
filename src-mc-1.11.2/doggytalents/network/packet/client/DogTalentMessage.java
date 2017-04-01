package doggytalents.network.packet.client;

import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class DogTalentMessage extends AbstractServerMessage {
	
	public int entityId;
	public String talentId;
	
	public DogTalentMessage() {}
    public DogTalentMessage(int entityId, String talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }
    
	@Override
	public void read(PacketBuffer buffer) {
		this.entityId = buffer.readInt();
		this.talentId = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
		ByteBufUtils.writeUTF8String(buffer, this.talentId);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		Entity target = player.world.getEntityByID(this.entityId);
        
        if(!(target instanceof EntityDog))
        	return;
        
		EntityDog dog = (EntityDog)target;
		int level = dog.talents.getLevel(this.talentId);
		ITalent talent = TalentRegistry.getTalent(this.talentId);
		
		
		if(level < talent.getHighestLevel(dog) && dog.spendablePoints() >= talent.getCost(dog, level + 1))
			dog.talents.setLevel(this.talentId, dog.talents.getLevel(this.talentId) + 1);
	}
}
