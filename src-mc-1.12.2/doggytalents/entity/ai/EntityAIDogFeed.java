package doggytalents.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import doggytalents.lib.Constants;

/**
 * Original code credit goes to the author of Sophisticated Wolves, NightKosh!
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntityAIDogFeed extends EntityAIBase {

	private EntityDog dog;
	private World world;
	protected EntityItem foodEntity;
	private float maxDist;
	private Random rand;

	public EntityAIDogFeed(EntityDog pet, float maxDistIn) {
		this.dog = pet;
		this.world = pet.world;
		this.maxDist = maxDistIn;
		this.rand = pet.world.rand;
	}

	@Override
	public boolean shouldExecute() {
		int order = this.dog.masterOrder();
		EntityItem foodItem = this.getNearestFoodItem();

		if(foodItem == null)
			return false;
		else if(this.dog.isSitting())
			return false;
		else if(this.dog.isIncapacicated())
			return false;
		else if(this.dog.getDogHunger() >= Constants.HUNGER_POINTS)
			return false;
		else if(order != 0) //Don't eat while being ordered
		return false;

		//Execute
		this.foodEntity = foodItem;
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		int order = this.dog.masterOrder();
		return this.dog.getDogHunger() < Constants.HUNGER_POINTS && this.isEating() && this.dog.getDistanceSq(this.foodEntity) < (double)(this.maxDist * this.maxDist) && !this.dog.isSitting() && order == 0;
	}

	@Override
	public void updateTask() {
		if (this.isEating()) {
			this.dog.getNavigator().tryMoveToEntityLiving(this.foodEntity, 1.0D);
			if (this.dog.getDistance(this.foodEntity) <= 1) {

				this.dog.getLookHelper().setLookPositionWithEntity(this.foodEntity, 10.0F, (float)this.dog.getVerticalFaceSpeed());
				
				//Eat
				this.dog.playSound(SoundEvents.ENTITY_PLAYER_BURP, this.dog.getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				int foodValue = this.dog.foodValue(this.foodEntity.getItem());
				this.dog.setDogHunger(this.dog.getDogHunger() + foodValue);
				ItemStack stack = foodEntity.getItem();
				if(stack.getItem() == ModItems.CHEW_STICK) {
					((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(this.dog);
				}

				this.foodEntity.getItem().shrink(1);
				if (this.foodEntity.getItem().isEmpty()) {
					this.foodEntity.setDead();
					this.foodEntity = null;
					this.dog.getNavigator().clearPath();
				}
			}
		}else{
			this.foodEntity = null;
			this.dog.getNavigator().clearPath();
		}
	}

	@Override
	public void resetTask() {
		this.foodEntity = null;
		this.dog.getNavigator().clearPath();
	}

	public EntityItem getNearestFoodItem() {
		List<EntityItem> foodList = this.world.getEntitiesWithinAABB(EntityItem.class, dog.getEntityBoundingBox().grow(this.maxDist, 4D, this.maxDist).expand(0.0D, (double) dog.world.getHeight(), 0.0D));
		for (EntityItem foodEntity : foodList) {
			ItemStack stack = foodEntity.getItem();
			int foodValue = this.dog.foodValue(stack);
			boolean isFish = (stack.getItem() == Items.FISH || stack.getItem() == Items.COOKED_FISH);
			int hunger = this.dog.getDogHunger();
			int bestHungerForFish = 110;

			if(foodValue != 0) {
				if(!isFish && (hunger >= bestHungerForFish || hunger < bestHungerForFish)) { //If the item is NOT fish AND the dog's hunger is either BELOW or ABOVE the bestHungerForFish value, allow the dog to eat the item
					return foodEntity;
				}else if(isFish && hunger >= bestHungerForFish){ //If the item IS fish AND the dog's hunger is ABOVE the bestHungerForFish value, do not allow the dog to eat the item
					return null;
				}else if(isFish && hunger < bestHungerForFish) { //If the item IS fish AND the dog's hunger is BELOW the bestHungerForFish value, allow the dog to eat the item
					return foodEntity;
				}
			}
		}
		return null;
	}

	public boolean isEating() {
		return this.foodEntity != null && this.foodEntity.isEntityAlive();
	}
}