package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;

public class OwnerHurtTargetGoal extends net.minecraft.entity.ai.goal.OwnerHurtTargetGoal {

    private DogEntity dog;

    public OwnerHurtTargetGoal(DogEntity dogIn) {
        super(dogIn);
        this.dog = dogIn;
    }

    @Override
    public boolean canUse() {
         return this.dog.isMode(EnumMode.AGGRESIVE, EnumMode.BERSERKER, EnumMode.TACTICAL) && super.canUse();
    }
}
