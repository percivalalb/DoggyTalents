package doggytalents.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;

public class EntityAIOwnerHurtByTargetDog extends EntityAIOwnerHurtByTarget {

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
