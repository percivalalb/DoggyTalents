package doggytalents.entity.ai;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.item.ItemThrowBone;
import net.minecraft.item.Item;

public class EntityAIFetch extends EntityAIClosestItem {

	public EntityAIFetch(EntityDog dogIn, double speedIn, float range) {
		super(dogIn, speedIn, range, item -> item.getItem() instanceof ItemThrowBone && ((ItemThrowBone)item.getItem()).type == ItemThrowBone.Type.DRY);
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
    
			if(this.dog.getDistanceSqToEntity(this.target) < (double)(1.5F * 1.5F) && !this.dog.hasBone()) {
				if(this.target.isEntityAlive()) {
					Item item = this.target.getItem().getItem();
	              		
					if(item == ModItems.THROW_BONE)
						this.dog.setBoneVariant(0);
					else if(item == ModItems.THROW_STICK)
						this.dog.setBoneVariant(1);
	            	
					this.dog.getNavigator().clearPathEntity();
					
					this.target.setDead();
				}
			}
	    }
	}
}