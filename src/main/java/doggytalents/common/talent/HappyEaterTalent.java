package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;

public class HappyEaterTalent extends Talent implements IDogFoodHandler {

    @Override
    public ActionResult<Float> setDogHunger(AbstractDogEntity dogIn, float hunger, float diff) {
        int level = dogIn.getLevel(this);
        hunger += diff / 10 * level;
        return ActionResult.resultSuccess(hunger);
    }

    @Override
    public boolean isFood(ItemStack stackIn) {
        Item item = stackIn.getItem();
        return item == Items.ROTTEN_FLESH || (item.isFood() && item.isIn(ItemTags.FISHES));
    }

    @Override
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        int level = dogIn.getLevel(this);

        if (level >= 3) {

            Item item = stackIn.getItem();

            if (item == Items.ROTTEN_FLESH) {
                return true;
            }

            if (level >= 5 && item.isFood() && item.isIn(ItemTags.FISHES)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        int level = dogIn.getLevel(this);

        if (level >= 3) {

            Item item = stackIn.getItem();

            if (item == Items.ROTTEN_FLESH) {
                dogIn.addHunger(30);
                dogIn.consumeItemFromStack(entityIn, stackIn);
                return true;
            }

            if (level >= 5 && item.isFood() && item.isIn(ItemTags.FISHES)) {
                dogIn.addHunger(item.getFood().getHealing() * 5);
                dogIn.consumeItemFromStack(entityIn, stackIn);
                return true;
            }
        }

        return false;
    }
}
