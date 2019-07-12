package doggytalents.entity.ai;

import java.util.EnumSet;
import java.util.List;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

public class EntityAIIncapacitatedTargetDog extends Goal {

    private final EntityDog dog;
    
    private int ticksExecuting;
    
    public EntityAIIncapacitatedTargetDog(EntityDog dogIn) {
        this.dog = dogIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }
    
    @Override
    public boolean shouldExecute() {
        if(!this.dog.isIncapacicated()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void startExecuting() {
        this.dog.setAttackTarget(null);
        this.dog.setRevengeTarget(null);
        this.dog.getNavigator().clearPath();
        
        List<LivingEntity> mobEntity = this.dog.world.getEntitiesWithinAABB(LivingEntity.class, this.dog.getBoundingBox().grow(16D, 4D, 16D));
        
        mobEntity.forEach(entity -> {
            if(entity.getRevengeTarget() == this.dog) {
                entity.setRevengeTarget(null);
            }
            
            if(entity instanceof MobEntity) {
                MobEntity mob = (MobEntity)entity;
                if(mob.getAttackTarget() == this.dog) {
                    mob.setAttackTarget(null);
                }
            }

        });
    }

    @Override
    public void resetTask() {
        this.ticksExecuting = 0;
    }
    
    @Override
    public void tick() {
        this.ticksExecuting++;
    }
}
