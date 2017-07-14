package doggytalents.entity.ai;

import java.util.Random;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
            if(this.dog.getAge() >= 100)
                return false;

            if(this.dog.getRNG().nextInt(this.dog.mode.isMode(EnumMode.WANDERING) ? 40 : 120) != 0)
                return false;
        }

        
        Vec3d vec3d = this.dog.mode.isMode(EnumMode.WANDERING) ? generateRandomPos(this.dog) : this.getPosition(10);

        if (vec3d == null)
            return false;
        else {
            this.x = vec3d.xCoord;
            this.y = vec3d.yCoord;
            this.z = vec3d.zCoord;
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
 
    	
    	int x = random.nextInt(11) - 5;
    	int z = random.nextInt(11) - 5;
    	
    	return new Vec3d(bowlPos.getX() + x, bowlPos.getY(), bowlPos.getZ() + z);
    }
    
    private static BlockPos moveAboveSolid(BlockPos p_191378_0_, EntityCreature p_191378_1_)
    {
        if (!p_191378_1_.world.getBlockState(p_191378_0_).getMaterial().isSolid())
        {
            return p_191378_0_;
        }
        else
        {
            BlockPos blockpos;

            for (blockpos = p_191378_0_.up(); blockpos.getY() < p_191378_1_.world.getHeight() && p_191378_1_.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up())
            {
                ;
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos p_191380_0_, EntityCreature p_191380_1_)
    {
        return p_191380_1_.world.getBlockState(p_191380_0_).getMaterial() == Material.WATER;
    }

    @Override
    public boolean continueExecuting() {
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