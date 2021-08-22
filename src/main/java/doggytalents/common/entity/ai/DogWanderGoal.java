package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class DogWanderGoal extends Goal {

    protected final DogEntity dog;

    protected final double speed;
    protected int executionChance;

    public DogWanderGoal(DogEntity dogIn, double speedIn) {
        this.dog = dogIn;
        this.speed = speedIn;
        this.executionChance = 60;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.dog.isTame() || this.dog.isVehicle()) {
            return false;
        }

        if (!this.dog.isMode(EnumMode.WANDERING)) {
            return false;
        }

        Optional<BlockPos> bowlPos = this.dog.getBowlPos();

        if (!bowlPos.isPresent()) {
            return false;
        }

        return bowlPos.get().distSqr(this.dog.blockPosition()) < 400.0D;
    }

    @Override
    public void tick() {
        if (this.dog.getNoActionTime() >= 100) {
            return;
        } else if (this.dog.getRandom().nextInt(this.executionChance) != 0) {
            return;
        } if (this.dog.isPathFinding()) {
            return;
        }

        Vec3 pos = this.getPosition();
        this.dog.getNavigation().moveTo(pos.x, pos.y, pos.z, this.speed);
    }

    @Nullable
    protected Vec3 getPosition() {
        PathNavigation pathNavigate = this.dog.getNavigation();
        Random random = this.dog.getRandom();

        int xzRange = 5;
        int yRange = 3;

        float bestWeight = Float.MIN_VALUE;
        Optional<BlockPos> bowlPos = this.dog.getBowlPos();
        BlockPos bestPos = bowlPos.get();

        for (int attempt = 0; attempt < 5; ++attempt) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            BlockPos testPos = bowlPos.get().offset(l, i1, j1);

            if (pathNavigate.isStableDestination(testPos)) {
                float weight = this.dog.getWalkTargetValue(testPos);

                if (weight > bestWeight) {
                    bestWeight = weight;
                    bestPos = testPos;
                }
            }
        }

        return new Vec3(bestPos.getX(), bestPos.getY(), bestPos.getZ());
    }
}
