package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import doggytalents.common.util.EntityUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;
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
            this.spawnBaby();
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

    protected void spawnBaby() {
        AgeableEntity ageableentity = this.animal.createChild(this.targetMate);
        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(animal, targetMate, ageableentity);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        ageableentity = event.getChild();
        if (cancelled) {
            //Reset the "inLove" state for the animals
            this.animal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            return;
        }
        if (ageableentity != null) {
            ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
            if (serverplayerentity == null && this.targetMate.getLoveCause() != null) {
                serverplayerentity = this.targetMate.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.addStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, ageableentity);
            }

            this.animal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            ageableentity.setGrowingAge(-24000);
            ageableentity.setLocationAndAngles(this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), 0.0F, 0.0F);
            this.world.addEntity(ageableentity);
            this.world.setEntityState(this.animal, (byte)18);
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.addEntity(new ExperienceOrbEntity(this.world, this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), this.animal.getRNG().nextInt(7) + 1));
            }

        }
    }
}
