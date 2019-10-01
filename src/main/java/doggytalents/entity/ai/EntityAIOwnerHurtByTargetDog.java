package doggytalents.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;

public class EntityAIOwnerHurtByTargetDog extends OwnerHurtByTargetGoal {

    private EntityDog dog;
    
    public EntityAIOwnerHurtByTargetDog(EntityDog dogIn) {
        super(dogIn);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
         return (this.dog.MODE.isMode(EnumMode.AGGRESIVE) || this.dog.MODE.isMode(EnumMode.BERSERKER)) && super.shouldExecute();
    }
}
