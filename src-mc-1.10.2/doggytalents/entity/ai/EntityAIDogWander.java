package doggytalents.entity.ai;

import java.util.Random;

import javax.annotation.Nullable;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIDogWander extends EntityAIBase {
	
	protected final EntityDog dog;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;
    
	protected boolean wandering;

	public EntityAIDogWander(EntityDog dogIn, double speedIn) {
		this.dog = dogIn;
		this.speed = speedIn;
		this.executionChance = 120;
		this.wandering = false;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		this.wandering = this.dog.canWander();
		this.setExecutionChance(this.wandering ? 40 : 120);

		if (this.dog.getAge() >= 100) {
			return false;
		}

		if (this.dog.getRNG().nextInt(this.executionChance) != 0)
		{
			return false;
		}
		
		 Vec3d vec3d = this.wandering ? this.generateRandomPos(this.dog) : RandomPositionGenerator.findRandomTarget(this.dog, 10, 7);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec3d.xCoord;
            this.yPosition = vec3d.yCoord;
            this.zPosition = vec3d.zCoord;
            this.mustUpdate = false;
            return true;
        }
	}
	
	@Override
	public boolean continueExecuting()
    {
        return !this.dog.getNavigator().noPath();
    }

	@Override
    public void startExecuting()
    {
        this.dog.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
	
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }
	
	private Vec3d generateRandomPos(EntityDog dog) {
        PathNavigate pathnavigate = dog.getNavigator();
    	Random random = dog.getRNG();
 
    	int xzRange = 5;
    	int yRange = 3;
    	
    	float bestWeight = -99999.0F;
    	BlockPos bestPos = dog.COORDS.getBowlPos();
    	
    	for(int attempt = 0; attempt < 10; ++attempt) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            BlockPos testPos = dog.COORDS.getBowlPos().add(l, i1, j1);

            if(pathnavigate.canEntityStandOnPos(testPos)) {
            	float weight = dog.getBlockPathWeight(testPos);

            	if(weight > bestWeight) {
            		bestWeight = weight;
            		bestPos = testPos;
            	}
            }
        }
    	
    	return new Vec3d(bestPos.getX(), bestPos.getY(), bestPos.getZ());
    }
}