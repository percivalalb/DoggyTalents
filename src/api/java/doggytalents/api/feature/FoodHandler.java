package doggytalents.api.feature;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class FoodHandler {

    private static final List<IDogFoodHandler> HANDLERS = Lists.newArrayList();

    public static void registerHandler(IDogFoodHandler handler) {
        HANDLERS.add(handler);
    }

    public static Optional<IDogFoodHandler> isFood(ItemStack stackIn) {
        if (stackIn.getItem() instanceof IDogFoodHandler) {
            if (((IDogFoodHandler) stackIn.getItem()).isFood(stackIn)) {
                return Optional.of((IDogFoodHandler) stackIn.getItem());
            }
        }

        for (IDogFoodHandler handler : HANDLERS) {
            if (handler.isFood(stackIn)) {
                return Optional.of(handler);
            }
        }

        return Optional.empty();
    }

    public static Optional<IDogFoodHandler> getMatch(@Nullable DogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn) {
        if (stackIn.getItem() instanceof IDogFoodHandler) {
            if (((IDogFoodHandler) stackIn.getItem()).canConsume(dogIn, stackIn, entityIn)) {
                return Optional.of((IDogFoodHandler) stackIn.getItem());
            }
        }

        for (IDogFoodHandler handler : HANDLERS) {
            if (handler.canConsume(dogIn, stackIn, entityIn)) {
                return Optional.of(handler);
            }
        }

        return Optional.empty();
    }
}
