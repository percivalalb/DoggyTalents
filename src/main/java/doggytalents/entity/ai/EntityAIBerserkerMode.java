package doggytalents.entity.ai;

import com.google.common.base.Predicate;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityAIBerserkerMode<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

    private EntityDog dog;
    
    public EntityAIBerserkerMode(EntityDog dogIn, Class<T> classTarget, boolean checkSight) {
        super(dogIn, classTarget, checkSight, false);
        this.dog = dogIn;
    }
    
    public EntityAIBerserkerMode(EntityDog dogIn, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, Predicate<T> targetSelector) {
        super(dogIn, classTarget, chance, checkSight, onlyNearby, targetSelector);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.MODE.isMode(EnumMode.BERSERKER) && super.shouldExecute();
    }
}
