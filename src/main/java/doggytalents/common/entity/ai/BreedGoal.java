package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import doggytalents.common.util.EntityUtil;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BreedGoal extends Goal {

    private static final EntityPredicate breedPredicate = (new EntityPredicate()).setDistance(8.0D).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired().setSkipAttackChecks();
    private final AnimalEntity animal;
    private final Class<? extends AnimalEntity> mateClass;
    private final World world;
    private final double moveSpeed;

    protected AnimalEntity targetMate;
    private int spawnBabyDelay;

    public BreedGoal(AnimalEntity animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public BreedGoal(AnimalEntity animal, double moveSpeed, Class<? extends AnimalEntity> mateClass) {
        this.animal = animal;
        this.world = animal.world;
        this.mateClass = mateClass;
        this.moveSpeed = moveSpeed;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.animal.getLookController().setLookPositionWithEntity(this.targetMate, 10.0F, this.animal.getVerticalFaceSpeed());
        this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0D) {
            this.animal.func_234177_a_((ServerWorld) this.world, this.targetMate);
        }
    }

    @Nullable
    private AnimalEntity getNearbyMate() {
        List<AnimalEntity> entities = this.world.getEntitiesWithinAABB(
            this.mateClass, this.animal.getBoundingBox().grow(8.0D), this::filterEntities
        );
        return EntityUtil.getClosestTo(this.animal, entities);
    }

    private boolean filterEntities(AnimalEntity entity) {
        return breedPredicate.canTarget(this.animal, entity) && this.animal.canMateWith(entity);
    }
}
