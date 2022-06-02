package doggytalents.common.entity;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MeatFoodHandler implements IDogFoodHandler {

    @Override
    public boolean isFood(ItemStack stackIn) {
        return stackIn.isEdible() && stackIn.getItem().getFoodProperties().isMeat() && stackIn.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public boolean canConsume(AbstractDog dogIn, ItemStack stackIn, Entity entityIn) {
        return this.isFood(stackIn);
    }

    @Override
    public InteractionResult consume(AbstractDog dogIn, ItemStack stackIn, Entity entityIn) {

        if (dogIn.getDogHunger() < dogIn.getMaxHunger()) {
            if (!dogIn.level.isClientSide) {
                int heal = stackIn.getItem().getFoodProperties().getNutrition() * 5;

                dogIn.setDogHunger(dogIn.getDogHunger() + heal);
                dogIn.consumeItemFromStack(entityIn, stackIn);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;

    }

}
