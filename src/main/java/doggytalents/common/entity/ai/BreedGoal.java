package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import doggytalents.common.util.EntityUtil;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class BreedGoal extends Goal {

    private static final TargetingConditions breedPredicate = (new TargetingConditions()).range(8.0D).allowInvulnerable().allowSameTeam().allowUnseeable().allowNonAttackable();
    private final Animal animal;
    private final Class<? extends Animal> mateClass;
    private final Level world;
    private final double moveSpeed;

    protected Animal targetMate;
    private int spawnBabyDelay;

    public BreedGoal(Animal animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public BreedGoal(Animal animal, double moveSpeed, Class<? extends Animal> mateClass) {
        this.animal = animal;
        this.world = animal.level;
        this.mateClass = mateClass;
        this.moveSpeed = moveSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void stop() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.animal.getLookControl().setLookAt(this.targetMate, 10.0F, this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.animal.distanceToSqr(this.targetMate) < 9.0D) {
            this.animal.spawnChildFromBreeding((ServerLevel) this.world, this.targetMate);
        }
    }

    @Nullable
    private Animal getNearbyMate() {
        List<Animal> entities = this.world.getEntitiesOfClass(
            this.mateClass, this.animal.getBoundingBox().inflate(8.0D), this::filterEntities
        );
        return EntityUtil.getClosestTo(this.animal, entities);
    }

    private boolean filterEntities(Animal entity) {
        return breedPredicate.test(this.animal, entity) && this.animal.canMate(entity);
    }
}
