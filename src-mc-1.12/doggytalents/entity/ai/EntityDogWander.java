package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityDogWander extends EntityAIBase {
	
    protected final EntityDog dog;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected boolean mustUpdate;

    public EntityDogWander(EntityDog dog, double speedIn) {
        this.dog = dog;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(!this.mustUpdate) {
            if(this.dog.getIdleTime() >= 100)
                return false;

            if(this.dog.getRNG().nextInt(this.dog.mode.isMode(EnumMode.WANDERING) ? 60 : 120) != 0)
                return false;
        }

        Vec3d vec3d = this.getPosition();

        if (vec3d == null)
            return false;
        else {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            this.mustUpdate = false;
            return true;
        }
    }

    protected Vec3d getPosition() {
        return RandomPositionGenerator.findRandomTarget(this.dog, 10, 7);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.dog.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.dog.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }
}