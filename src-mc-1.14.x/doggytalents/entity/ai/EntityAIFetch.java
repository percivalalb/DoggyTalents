package doggytalents.entity.ai;

import java.util.function.Predicate;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.item.ItemThrowBone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAIFetch extends EntityAIClosestItem {

    private static Predicate<ItemStack> BONE_PREDICATE = (item) -> item.getItem() instanceof ItemThrowBone && ((ItemThrowBone)item.getItem()).type == ItemThrowBone.Type.DRY;
    
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
    
			if(this.dog.getDistanceSq(this.target) < (double)(1.5F * 1.5F) && !this.dog.hasBone()) {
				if(this.target.isAlive() && !this.target.cannotPickup()) {
					Item item = this.target.getItem().getItem();
	              		
					if(item == ModItems.THROW_BONE)
						this.dog.setBoneVariant(0);
					else if(item == ModItems.THROW_STICK)
						this.dog.setBoneVariant(1);
	            	
					this.dog.getNavigator().clearPath();
					
					this.target.remove();
				}
			}
	    }
	}
}