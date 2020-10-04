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
import net.minecraft.util.math.vector.Vector3d;

public class DogWanderGoal extends Goal {

    protected final DogEntity dog;

    protected final double speed;
    protected int executionChance;

    public DogWanderGoal(DogEntity dogIn, double speedIn) {
        this.dog = dogIn;
        this.speed = speedIn;
        this.executionChance = 60;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dog.isTamed() || this.dog.isBeingRidden()) {
            return false;
        }

        if (!this.dog.isMode(EnumMode.WANDERING)) {
            return false;
        }

        Optional<BlockPos> bowlPos = this.dog.getBowlPos();

        if (!bowlPos.isPresent()) {
            return false;
        }

        return bowlPos.get().distanceSq(this.dog.getPosition()) < 400.0D;
    }

    @Override
    public void tick() {
        if (this.dog.getIdleTime() >= 100) {
            return;
        } else if (this.dog.getRNG().nextInt(this.executionChance) != 0) {
            return;
        } if (this.dog.hasPath()) {
            return;
        }

        Vector3d pos = this.getPosition();
        this.dog.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, this.speed);
    }

    @Nullable
    protected Vector3d getPosition() {
        PathNavigator pathNavigate = this.dog.getNavigator();
        Random random = this.dog.getRNG();

        int xzRange = 5;
        int yRange = 3;

        float bestWeight = Float.MIN_VALUE;
        Optional<BlockPos> bowlPos = this.dog.getBowlPos();
        BlockPos bestPos = bowlPos.get();

        for (int attempt = 0; attempt < 5; ++attempt) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            BlockPos testPos = bowlPos.get().add(l, i1, j1);

            if (pathNavigate.canEntityStandOnPos(testPos)) {
                float weight = this.dog.getBlockPathWeight(testPos);

                if (weight > bestWeight) {
                    bestWeight = weight;
                    bestPos = testPos;
                }
            }
        }

        return new Vector3d(bestPos.getX(), bestPos.getY(), bestPos.getZ());
    }
}
