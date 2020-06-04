package doggytalents.common.talent;

import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.inferface.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class HappyEaterTalent extends Talent implements IDogFoodHandler {

    @Override
    public ActionResult<Float> setDogHunger(DogEntity dogIn, float hunger, float diff) {
        int level = dogIn.getLevel(this);
        hunger += diff / 10 * level;
        return ActionResult.resultSuccess(hunger);
    }

    @Override
    public boolean isFood(ItemStack stackIn) {
        Item item = stackIn.getItem();
        return item == Items.COD || item == Items.COOKED_COD || item == Items.SALMON || item == Items.COOKED_SALMON || item == Items.TROPICAL_FISH || item == Items.ROTTEN_FLESH;
    }

    @Override
    public boolean canConsume(DogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        Item item = stackIn.getItem();

        int level = dogIn.getLevel(this);
        if ((item == Items.COD || item == Items.COOKED_COD || item == Items.SALMON || item == Items.COOKED_SALMON || item == Items.TROPICAL_FISH) && level >= 5) {
            return true;
        }

        if (item == Items.ROTTEN_FLESH && level >= 3) {
            return true;
        }

        return false;
    }

    @Override
    public boolean consume(DogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        Item item = stackIn.getItem();
        int level = dogIn.getLevel(this);
        if (dogIn.getDogHunger() < dogIn.getMaxHunger()) {
            if ((item == Items.COD || item == Items.COOKED_COD || item == Items.SALMON || item == Items.COOKED_SALMON || item == Items.TROPICAL_FISH) && level >= 5) {
                dogIn.addHunger(30);
                dogIn.consumeItemFromStack(entityIn, stackIn);
            }

            if (item == Items.ROTTEN_FLESH && level >= 3) {
                dogIn.addHunger(30);
                dogIn.consumeItemFromStack(entityIn, stackIn);
            }

            return true;
        }

        return false;
    }
}
