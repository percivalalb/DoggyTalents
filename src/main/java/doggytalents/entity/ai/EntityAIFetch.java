package doggytalents.entity.ai;

import java.util.function.Predicate;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.entity.EntityDog;
import net.minecraft.item.ItemStack;

public class EntityAIFetch extends EntityAIClosestItem {

    public static Predicate<ItemStack> BONE_PREDICATE = (item) -> item.getItem() instanceof IThrowableItem;
    
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
    public void tick() {
        super.tick();
        if(!this.dog.isSitting()) {
    
            if(this.dog.getDistance(this.target) < 2F && !this.dog.hasBone()) {
                if(this.target.isAlive() && !this.target.cannotPickup()) {

                    this.dog.setBoneVariant(this.target.getItem());
                    
                    this.dog.getNavigator().clearPath();
                    this.target.remove();
                }
            }
        }
    }
}