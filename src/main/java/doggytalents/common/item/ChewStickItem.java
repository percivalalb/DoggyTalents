package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.item.Item.Properties;

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
    public InteractionResult consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        if (!dogIn.level.isClientSide) {
            dogIn.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1, false, true));
            dogIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 6, false, true));
            dogIn.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, false, true));
            dogIn.consumeItemFromStack(entityIn, stackIn);
        }

        return InteractionResult.SUCCESS;
    }

}
