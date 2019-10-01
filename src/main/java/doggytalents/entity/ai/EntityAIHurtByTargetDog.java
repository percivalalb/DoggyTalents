package doggytalents.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

public class EntityAIHurtByTargetDog extends HurtByTargetGoal {

    private EntityDog dog;
    
    public EntityAIHurtByTargetDog(EntityDog dogIn) {
        super(dogIn);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
         return (this.dog.MODE.isMode(EnumMode.AGGRESIVE) || this.dog.MODE.isMode(EnumMode.BERSERKER) || this.dog.MODE.isMode(EnumMode.TACTICAL)) && super.shouldExecute();
    }
}
