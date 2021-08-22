package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;

public class OwnerHurtByTargetGoal extends net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal {

    private DogEntity dog;

    public OwnerHurtByTargetGoal(DogEntity dogIn) {
        super(dogIn);
        this.dog = dogIn;
    }

    @Override
    public boolean canUse() {
         return this.dog.isMode(EnumMode.AGGRESIVE, EnumMode.BERSERKER) && super.canUse();
    }
}
