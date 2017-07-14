package doggytalents.entity.ai;

import java.util.Random;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIDogWander extends EntityAIBase {
	
    protected final EntityDog dog;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected boolean mustUpdate;

    public EntityAIDogWander(EntityDog dog, double speedIn) {
        this.dog = dog;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(!this.mustUpdate) {
            if(this.dog.getIdleTime() >= 100)
                return false;

            if(this.dog.getRNG().nextInt(this.dog.mode.isMode(EnumMode.WANDERING) ? 40 : 120) != 0)
                return false;
        }

        
        Vec3d vec3d = this.dog.mode.isMode(EnumMode.WANDERING) ? generateRandomPos(this.dog) : this.getPosition(10);

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

    protected Vec3d getPosition(int xz) {
        return RandomPositionGenerator.findRandomTarget(this.dog, xz, 7);
    }
    
    private static Vec3d generateRandomPos(EntityDog dog) {
        PathNavigate pathnavigate = dog.getNavigator();
    	Random random = dog.getRNG();
    	BlockPos bowlPos = dog.coords.getBowlPos();
 
    	int xzRange = 5;
    	int yRange = 6;
    	
    	float bestWeight = -99999.0F;
    	int x = 0, y = 0, z = 0;
    	
    	for (int attempt = 0; attempt < 10; ++attempt) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            BlockPos testPos = new BlockPos(l + bowlPos.getX(), i1 + bowlPos.getY(), j1 + bowlPos.getZ());

            if(pathnavigate.canEntityStandOnPos(testPos)) {
            	float weight = dog.getBlockPathWeight(testPos);

            	if(weight > bestWeight) {
            		bestWeight = weight;
            		x = l;
            		y = i1;
            		z = j1;
            	}
            }
        }
    	
    	return new Vec3d(bowlPos.getX() + x, bowlPos.getY() + y, bowlPos.getZ() + z);
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