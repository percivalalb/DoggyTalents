package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogTalent {
	
	public int entityId;
	public String talentId;
	
    public PacketDogTalent(int entityId, String talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }
    
	public static void encode(PacketDogTalent msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeString(msg.talentId, 64);
	}
	
	public static PacketDogTalent decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		String talentId = buf.readString(64);
		return new PacketDogTalent(entityId, talentId);
	}
	
	
	public static class Handler {
        public static void handle(final PacketDogTalent message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);

                if(!(target instanceof EntityDog))
                	return;

        		EntityDog dog = (EntityDog)target;
        		int level = dog.TALENTS.getLevel(message.talentId);
        		ITalent talent = TalentRegistry.getTalent(message.talentId);
        		
        		if(level < talent.getHighestLevel(dog) && dog.spendablePoints() >= talent.getCost(dog, level + 1))
        			dog.TALENTS.setLevel(message.talentId, dog.TALENTS.getLevel(message.talentId) + 1);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
