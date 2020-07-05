package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;

public class OwnerHurtByTargetGoal extends net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal {

    private DogEntity dog;

    public OwnerHurtByTargetGoal(DogEntity dogIn) {
        super(dogIn);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
         return this.dog.isMode(EnumMode.AGGRESIVE, EnumMode.BERSERKER) && super.shouldExecute();
    }
}
