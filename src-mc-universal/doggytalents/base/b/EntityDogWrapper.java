package doggytalents.base.b;

import java.util.UUID;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.world.World;

public class EntityDogWrapper extends EntityDog {

	public EntityDogWrapper(World world) {
		super(world);
	}

	@Override
	public void addAIMeleeAttack(int priority, double speedIn, boolean useLongMemory) {
		this.tasks.addTask(priority, new EntityAIAttackOnCollide(this, speedIn, useLongMemory)); 	
	}
	
	@Override
	public boolean isBeingRidden() {
		return this.riddenByEntity != null;
	}
	
	@Override
	public void setOwnerUUID(UUID uuid) {
		this.setOwnerId(uuid.toString());
	}
	
	@Override
	public UUID getOwnerUUID() {
		return UUID.fromString(this.getOwnerId());
	}
}
