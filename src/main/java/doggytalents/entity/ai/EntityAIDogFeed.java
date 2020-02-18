package doggytalents.entity.ai;

import java.util.Random;
import java.util.function.BiPredicate;

import doggytalents.api.inferface.IDogFoodItem;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;

public class EntityAIDogFeed extends EntityAIClosestItem {

    private Random rand;
    private static final BiPredicate<EntityDog, ItemStack> DOG_CONDITIONS = (dog, stack) -> {
        int foodValue = dog.foodValue(stack, null);
        if (foodValue <= 0) return false;
        return (dog.getHungerFeature().getMaxHunger() - dog.getDogHunger()) * 3 >= foodValue * 2 
                || dog.getDogHunger() / (float)dog.getHungerFeature().getMaxHunger() <= 0.5F;
    };

    public EntityAIDogFeed(EntityDog dogIn, double speedIn, float range) {
        super(dogIn, speedIn, range, stack -> DOG_CONDITIONS.test(dogIn, stack));
        this.rand = dogIn.world.rand;
    }

    @Override
    public boolean shouldExecute() {
        // Still add the AI task this allows config option to be changed without restart
        return ConfigValues.EAT_FOOD_ON_FLOOR && !this.dog.isSitting() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ConfigValues.EAT_FOOD_ON_FLOOR && !this.dog.isSitting() && super.shouldContinueExecuting() && DOG_CONDITIONS.test(this.dog, this.target.getItem());
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.dog.isSitting()) {

            if (this.dog.getDistance(this.target) <= 1) {

                this.dog.getLookController().setLookPositionWithEntity(this.target, 10.0F, this.dog.getVerticalFaceSpeed());

                //Eat
                this.dog.playSound(SoundEvents.ENTITY_PLAYER_BURP, this.dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                
                ItemStack stack = this.target.getItem();
                int foodValue = this.dog.foodValue(stack, null);
                this.dog.setDogHunger(this.dog.getDogHunger() + foodValue);
  
                if (stack.getItem() instanceof IDogFoodItem) {
                    IDogFoodItem dogFood = (IDogFoodItem)stack.getItem();
                    dogFood.onItemConsumed(this.dog, stack, null);
                }

                this.target.getItem().shrink(1);
                if (this.target.getItem().isEmpty()) {
                    this.target.remove();
                    this.target = null;
                    this.dog.getNavigator().clearPath();
                }
            }
        }
    }
}