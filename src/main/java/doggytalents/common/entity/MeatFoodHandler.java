package doggytalents.common.entity;

import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MeatFoodHandler implements IDogFoodHandler {

    @Override
    public boolean isFood(ItemStack stackIn) {
        return stackIn.isFood() && stackIn.getItem().getFood().isMeat() && stackIn.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public boolean canConsume(DogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        return stackIn.isFood() && stackIn.getItem().getFood().isMeat() && stackIn.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public boolean consume(DogEntity dogIn, ItemStack stackIn, Entity entityIn) {

        if (dogIn.getDogHunger() < dogIn.getMaxHunger()) {
            if (!dogIn.world.isRemote) {

                int heal = stackIn.getItem().getFood().getHealing() * 5;

                //dogIn.setHealth(dogIn.getHealth() + heal);
                dogIn.setDogHunger(dogIn.getDogHunger() + heal);
                dogIn.consumeItemFromStack(entityIn, stackIn);
            }

            return true;
        }

        return false;

    }

}
