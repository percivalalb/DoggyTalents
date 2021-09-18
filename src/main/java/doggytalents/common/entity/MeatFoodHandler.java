package doggytalents.common.entity;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class MeatFoodHandler implements IDogFoodHandler {

    @Override
    public boolean isFood(ItemStack stackIn) {
        return stackIn.isEdible() && stackIn.getItem().getFoodProperties().isMeat() && stackIn.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        return this.isFood(stackIn);
    }

    @Override
    public ActionResultType consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {

        if (dogIn.getDogHunger() < dogIn.getMaxHunger()) {
            if (!dogIn.level.isClientSide) {
                int heal = stackIn.getItem().getFoodProperties().getNutrition() * 5;

                dogIn.setDogHunger(dogIn.getDogHunger() + heal);
                dogIn.consumeItemFromStack(entityIn, stackIn);
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;

    }

}
