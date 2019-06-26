package doggytalents.entity.ai;

import java.util.List;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIIncapacitatedTargetDog extends EntityAIBase {

    private final EntityDog dog;
    
    private int ticksExecuting;
    
    public EntityAIIncapacitatedTargetDog(EntityDog dogIn) {
        this.dog = dogIn;
        this.setMutexBits(1);
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
        
        List<EntityLivingBase> mobEntity = this.dog.world.getEntitiesWithinAABB(EntityLivingBase.class, this.dog.getEntityBoundingBox().grow(16D, 4D, 16D));
        
        mobEntity.forEach(entity -> {
            if(entity.getRevengeTarget() == this.dog) {
                entity.setRevengeTarget(null);
            }
            
            if(entity instanceof EntityLiving) {
                EntityLiving mob = (EntityLiving)entity;
                if(mob.getAttackTarget() == this.dog) {
                    mob.setAttackTarget(null);
                }
            }

        });
    }

    @Override
    public void resetTask() {
        DoggyTalents.LOGGER.debug("Reset task");
        this.ticksExecuting = 0;
    }
    
    @Override
    public void updateTask() {
        this.ticksExecuting++;
    }
}
