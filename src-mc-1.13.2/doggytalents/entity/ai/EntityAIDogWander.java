package doggytalents.entity.ai;

import java.util.Random;

import javax.annotation.Nullable;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIDogWander extends EntityAIWander {
	
	protected EntityDog dog;
	protected final float probability;

	public EntityAIDogWander(EntityDog dogIn, double p_i47301_2_) {
		this(dogIn, p_i47301_2_, 0.001F);
	}

	public EntityAIDogWander(EntityDog dogIn, double p_i47302_2_, float p_i47302_4_) {
		super(dogIn, p_i47302_2_);
		this.dog = dogIn;
		this.probability = p_i47302_4_;
	}

	@Override
	@Nullable
	protected Vec3d getPosition() {
		if(this.entity.isInWaterOrBubbleColumn()) {
			Vec3d vec3d = RandomPositionGenerator.getLandPos(this.entity, 15, 7);
			return vec3d == null ? super.getPosition() : vec3d;
		} 
		else {
			return this.entity.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.entity, 10, 7) : super.getPosition();
		}
	}
	
	private static Vec3d generateRandomPos(EntityDog dog) {
		
        PathNavigate pathnavigate = dog.getNavigator();
    	Random random = dog.getRNG();
    	int bowlPosX = dog.COORDS.getBowlPos().getX();
    	int bowlPosY = dog.COORDS.getBowlPos().getY();
    	int bowlPosZ = dog.COORDS.getBowlPos().getZ();
 
    	int xzRange = 5;
    	int yRange = 6;
    	
    	float bestWeight = -99999.0F;
    	int x = 0, y = 0, z = 0;
    	
    	for (int attempt = 0; attempt < 10; ++attempt) {
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