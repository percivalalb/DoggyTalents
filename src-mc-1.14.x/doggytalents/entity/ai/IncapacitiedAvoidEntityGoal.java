package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;

public class IncapacitiedAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final EntityDog dog;
    
    public IncapacitiedAvoidEntityGoal(EntityDog dogIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(dogIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
        this.dog = dogIn;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.dog.isIncapacicated() && super.shouldExecute();
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.dog.isIncapacicated() && super.shouldContinueExecuting();
    }
}
