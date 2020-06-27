package doggytalents.common.entity.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FindWaterGoal extends Goal {

    protected final CreatureEntity creature;
    private final double speed;
    private final World world;
    private final int searchLength;

    private double waterX;
    private double waterY;
    private double waterZ;

    public FindWaterGoal(CreatureEntity creatureIn, double speedIn) {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.world = creatureIn.world;
        this.searchLength = 14;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.creature.isBurning()) {
            return false;
        } else {
            Vec3d vec3d = this.findPossibleWater();
            if (vec3d == null) {
                return false;
            } else {
                this.waterX = vec3d.x;
                this.waterY = vec3d.y;
                this.waterZ = vec3d.z;
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.waterX, this.waterY, this.waterZ, this.speed);
    }

    @Nullable
    protected Vec3d findPossibleWater() {
        BlockPos blockpos = new BlockPos(this.creature);

        for (int y = 0; y <= 5; y = y > 0 ? -y : 1 - y) {
            for(int s = 0; s < this.searchLength; ++s) {
                for(int x = 0; x <= s; x = x > 0 ? -x : 1 - x) {
                    for(int z = x < s && x > -s ? s : 0; z <= s; z = z > 0 ? -z : 1 - z) {
                        BlockPos blockpos1 = blockpos.add(x, y - 1, z);

                        if(this.shouldMoveTo(this.creature.world, blockpos1)) {
                            return new Vec3d(blockpos1);
                        }
                    }
                }
            }
        }

        return null;
    }


    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if(worldIn.isRainingAt(pos)) {
            return true;
        } else {
            return this.isWaterDestination(worldIn, pos);
        }
    }

    protected boolean isWaterDestination(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }
}