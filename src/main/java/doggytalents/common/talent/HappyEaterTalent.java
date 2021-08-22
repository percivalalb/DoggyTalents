package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.inferface.IDogFoodPredicate;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

public class HappyEaterTalent extends TalentInstance implements IDogFoodHandler {

    public static final IDogFoodPredicate INNER_DYN_PRED = (stackIn) -> {
        Item item = stackIn.getItem();
        return item == Items.ROTTEN_FLESH || (item.isEdible() && item.is(ItemTags.FISHES));
    };

    public HappyEaterTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public ActionResult<Float> setDogHunger(AbstractDogEntity dogIn, float hunger, float diff) {
        hunger += diff / 10 * this.level();
        return ActionResult.success(hunger);
    }

    @Override
    public boolean isFood(ItemStack stackIn) {
        return HappyEaterTalent.INNER_DYN_PRED.isFood(stackIn);
    }

    @Override
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        if (this.level() >= 3) {

            Item item = stackIn.getItem();

            if (item == Items.ROTTEN_FLESH) {
                return true;
            }

            if (this.level() >= 5 && item.isEdible() && item.is(ItemTags.FISHES)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ActionResultType consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        if (this.level() >= 3) {

            Item item = stackIn.getItem();

            if (item == Items.ROTTEN_FLESH) {
                dogIn.addHunger(30);
                dogIn.consumeItemFromStack(entityIn, stackIn);
                return ActionResultType.SUCCESS;
            }

            if (this.level() >= 5 && item.isEdible() && item.is(ItemTags.FISHES)) {
                dogIn.addHunger(item.getFoodProperties().getNutrition() * 5);
                dogIn.consumeItemFromStack(entityIn, stackIn);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }
}
