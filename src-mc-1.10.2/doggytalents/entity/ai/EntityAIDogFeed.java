package doggytalents.entity.ai;

import java.util.Random;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import doggytalents.lib.Constants;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class EntityAIDogFeed extends EntityAIClosestItem {

	private Random rand;
	
	public EntityAIDogFeed(EntityDog dogIn, double speedIn, float range) {
		super(dogIn, speedIn, range, stack -> dogIn.foodValue(stack) > 0);
		this.rand = dogIn.world.rand;
	}

	@Override
	public boolean shouldExecute() {
		return !this.dog.isSitting() && super.shouldExecute();
	}

	@Override
	public boolean continueExecuting() {
		return this.dog.getDogHunger() < Constants.HUNGER_POINTS && !this.dog.isSitting() && super.continueExecuting();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if(!this.dog.isSitting()) {
    
			if (this.dog.getDistanceToEntity(this.target) <= 1) {

				this.dog.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float)this.dog.getVerticalFaceSpeed());
				
				//Eat
				this.dog.playSound(SoundEvents.ENTITY_PLAYER_BURP, this.dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				int foodValue = this.dog.foodValue(this.target.getEntityItem());
				this.dog.setDogHunger(this.dog.getDogHunger() + foodValue);
				ItemStack stack = target.getEntityItem();
				if(stack != null) {
					if(stack.getItem() == ModItems.CHEW_STICK) {
						((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(this.dog);
					}
	
					this.target.getEntityItem().stackSize--;
					if (this.target.getEntityItem().stackSize <= 0) {
						this.target.setDead();
						this.target = null;
						this.dog.getNavigator().clearPathEntity();
					}
				}
			}
	    }
	}
}