package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class BerserkerModeGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final DogEntity dog;

    public BerserkerModeGoal(DogEntity dogIn, Class<T> targetClassIn, boolean checkSight) {
        super(dogIn, targetClassIn, 10, checkSight, false,   //Set the third param to 10 because it is the default param if not specified
            (target) -> dogIn.wantsToAttack(target, dogIn.getOwner())  //Add predicate whick use the DogEntity::alterations dependent DogEntity::wantsToAttack method to filter out the target
        );
        this.dog = dogIn;
    }

    @Override
    public boolean canUse() {
        return this.dog.isMode(EnumMode.BERSERKER) && super.canUse();
    }
}