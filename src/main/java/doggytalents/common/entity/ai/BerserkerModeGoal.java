package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class BerserkerModeGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final DogEntity dog;

    public BerserkerModeGoal(DogEntity dogIn, Class<T> targetClassIn, boolean checkSight) {
        super(dogIn, targetClassIn, checkSight, false);
        this.dog = dogIn;
    }

    @Override
    public boolean canUse() {
        return this.dog.isMode(EnumMode.BERSERKER) && super.canUse();
    }
}