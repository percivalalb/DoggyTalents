package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WanderModeGoal extends Goal {

    protected final DogEntity dog;
    protected boolean wandering;

    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;

    public WanderModeGoal(DogEntity dogIn, double speedIn) {
        this.dog = dogIn;
        this.speed = speedIn;
        this.wandering = false;
        this.executionChance = 60;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.dog.isBeingRidden()) {
            return false;
        } else {
            if(this.dog.getIdleTime() >= 100) {
                return false;
            } else if(this.dog.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            } else if(!(this.dog.isTamed() && this.dog.isMode(EnumMode.WANDERING) && this.dog.getBowlPos().isPresent() && this.dog.getBowlPos().get().withinDistance(this.getPosition(), 20D))) {
                return false;
            }

            Vec3d vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                return true;
           }
        }
    }

    @Nullable
    protected Vec3d getPosition() {
        PathNavigator pathnavigate = dog.getNavigator();
        Random random = dog.getRNG();

        int xzRange = 5;
        int yRange = 3;

        float bestWeight = -99999.0F;
        Optional<BlockPos> bowlPosOptional = this.dog.getBowlPos();

        if (bowlPosOptional.isPresent()) {
            BlockPos bowlPos = bowlPosOptional.get();
            BlockPos bestPos = bowlPos;

            for(int attempt = 0; attempt < 10; ++attempt) {
                int l = random.nextInt(2 * xzRange + 1) - xzRange;
                int i1 = random.nextInt(2 * yRange + 1) - yRange;
                int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

                BlockPos testPos = bowlPos.add(l, i1, j1);

                if(pathnavigate.canEntityStandOnPos(testPos)) {
                    float weight = this.dog.getBlockPathWeight(testPos);

                    if(weight > bestWeight) {
                        bestWeight = weight;
                        bestPos = testPos;
                    }
                }
            }

            return new Vec3d(bestPos.getX(), bestPos.getY(), bestPos.getZ());
        }

        return null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.dog.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.dog.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }
}