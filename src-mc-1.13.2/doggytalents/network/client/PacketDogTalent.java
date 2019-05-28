package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogTalent {
	
	public int entityId;
	public ResourceLocation talentId;
	
    public PacketDogTalent(int entityId, ResourceLocation talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }
    
	public static void encode(PacketDogTalent msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeResourceLocation(msg.talentId);
	}
	
	public static PacketDogTalent decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		ResourceLocation talentId = buf.readResourceLocation();
		return new PacketDogTalent(entityId, talentId);
	}
	
	
	public static class Handler {
        public static void handle(final PacketDogTalent msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(msg.entityId);

                if(!(target instanceof EntityDog))
                	return;

        		EntityDog dog = (EntityDog)target;
        		Talent talent = DoggyTalentsAPI.TALENTS.getValue(msg.talentId);
        		int level = dog.TALENTS.getLevel(talent);
        		
        		if(level < talent.getHighestLevel(dog) && dog.spendablePoints() >= talent.getCost(dog, level + 1))
        			dog.TALENTS.setLevel(talent, level + 1);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
