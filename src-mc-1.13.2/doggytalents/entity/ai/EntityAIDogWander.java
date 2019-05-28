package doggytalents.entity.ai;

import java.util.Random;

import javax.annotation.Nullable;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIDogWander extends EntityAIWander {
	
	protected EntityDog dog;
	protected boolean wandering;

	public EntityAIDogWander(EntityDog dogIn, double speedIn) {
		super(dogIn, speedIn);
		this.dog = dogIn;
		this.wandering = false;
	}

	@Override
	public boolean shouldExecute() {
		this.wandering = this.dog.canWander();
		
		this.setExecutionChance(this.wandering ? 10 : 120);
		return super.shouldExecute();
	}
	
	@Override
	@Nullable
	protected Vec3d getPosition() {

		return this.wandering ?  this.generateRandomPos(this.dog) : super.getPosition();
	}
	
	private Vec3d generateRandomPos(EntityDog dog) {
		
        PathNavigate pathnavigate = dog.getNavigator();
    	Random random = dog.getRNG();
    	int bowlPosX = dog.COORDS.getBowlPos().getX();
    	int bowlPosY = dog.COORDS.getBowlPos().getY();
    	int bowlPosZ = dog.COORDS.getBowlPos().getZ();
 
    	int xzRange = 5;
    	int yRange = 3;
    	
    	float bestWeight = -99999.0F;
    	int x = 0, y = 0, z = 0;
    	
    	for(int attempt = 0; attempt < 10; ++attempt) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            BlockPos testPos = new BlockPos(l + bowlPosX, i1 + bowlPosY, j1 + bowlPosZ);

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
    	
    	return new Vec3d(bowlPosX + x, bowlPosY + y, bowlPosZ + z);
    }
}