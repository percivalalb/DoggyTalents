package doggytalents.entity.ai;

import com.google.common.base.Predicate;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.entity.EntityDog;
import net.minecraft.item.ItemStack;

public class EntityAIFetch extends EntityAIClosestItem {

    private static Predicate<ItemStack> BONE_PREDICATE = (item) -> item.getItem() instanceof IThrowableItem;
    
    public EntityAIFetch(EntityDog dogIn, double speedIn, float range) {
        super(dogIn, speedIn, range, BONE_PREDICATE);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dog.isSitting() && this.dog.MODE.isMode(EnumMode.DOCILE) && !this.dog.hasBone() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.dog.isSitting() && this.dog.MODE.isMode(EnumMode.DOCILE) && !this.dog.hasBone() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if(!this.dog.isSitting()) {
    
            if(this.dog.getDistanceSq(this.target) < (double)(1.5F * 1.5F) && !this.dog.hasBone()) {
                if(this.target.isEntityAlive() && !this.target.cannotPickup()) {

                    this.dog.setBoneVariant(this.target.getItem());
                    
                    this.dog.getNavigator().clearPath();
                    this.target.setDead();
                }
            }
        }
    }
}