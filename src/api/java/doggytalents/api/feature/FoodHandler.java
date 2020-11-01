package doggytalents.api.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class FoodHandler {

    private static final List<IDogFoodHandler> HANDLERS = Collections.synchronizedList(new ArrayList<>(4));

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

    public static Optional<IDogFoodHandler> getMatch(@Nullable AbstractDogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn) {
    	for (IDogFoodHandler handler : dogIn.getFoodHandlers()) {
            if (handler.canConsume(dogIn, stackIn, entityIn)) {
                return Optional.of(handler);
            }
    	}

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
