package doggytalents.handler;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntitySpawn {

	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof AbstractSkeletonEntity) {
			AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity)entity;
			skeleton.goalSelector.addGoal(3, new AvoidEntityGoal<>(skeleton, EntityDog.class, 6.0F, 1.0D, 1.2D));
		}
	}
}
