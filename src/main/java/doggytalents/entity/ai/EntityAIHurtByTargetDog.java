package doggytalents.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class EntityAIHurtByTargetDog extends EntityAIHurtByTarget {

    private EntityDog dog;
    
    public EntityAIHurtByTargetDog(EntityDog dogIn, boolean entityCallsForHelpIn) {
        super(dogIn, entityCallsForHelpIn);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
         return (this.dog.MODE.isMode(EnumMode.AGGRESIVE) || this.dog.MODE.isMode(EnumMode.BERSERKER) || this.dog.MODE.isMode(EnumMode.TACTICAL)) && super.shouldExecute();
    }
}
