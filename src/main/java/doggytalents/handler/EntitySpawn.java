package doggytalents.handler;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntitySpawn {

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        
        if(entity instanceof AbstractSkeleton) {
            AbstractSkeleton skeleton = (AbstractSkeleton)entity;
            skeleton.tasks.addTask(3, new EntityAIAvoidEntity<>(skeleton, EntityDog.class, 6.0F, 1.0D, 1.2D));
        }
    }
}
