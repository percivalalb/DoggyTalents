package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ChewStickItem extends Item implements IDogFoodHandler {

    public ChewStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFood(ItemStack stackIn) {
        return stackIn.getItem() == this;
    }

    @Override
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        return true;
    }

    @Override
    public boolean consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        if (!dogIn.world.isRemote) {
            dogIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 1, false, true));
            dogIn.addPotionEffect(new EffectInstance(Effects.SPEED, 200, 6, false, true));
            dogIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 2, false, true));
            dogIn.consumeItemFromStack(entityIn, stackIn);
        }

        return true;
    }

}
