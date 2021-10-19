package doggytalents.common.entity.ai;

import java.util.function.Predicate;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;

public class BerserkerModeGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final DogEntity dog;

    public BerserkerModeGoal(DogEntity dogIn, Class<T> targetClassIn, boolean checkSight) {
        super(dogIn, targetClassIn, 10, checkSight, false,
            (target) -> !(target instanceof CreeperEntity)
        );
        this.dog = dogIn;
    }

    @Override
    public boolean canUse() {
        return this.dog.isMode(EnumMode.BERSERKER) && super.canUse();
    }

    public void setNewSelector(Predicate<LivingEntity> ple) {
        this.targetConditions.selector(ple);
    }
}