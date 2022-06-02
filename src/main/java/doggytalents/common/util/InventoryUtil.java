package doggytalents.common.util;

import doggytalents.api.feature.FoodHandler;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogFoodHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class InventoryUtil {

    public static InteractionResult feedDogFrom(AbstractDog dogIn, @Nullable Entity entity, IItemHandlerModifiable source) {

        for (int i = 0; i < source.getSlots(); i++) {

            ItemStack stack = source.getStackInSlot(i).copy();
            Optional<IDogFoodHandler> foodHandler = FoodHandler.getMatch(dogIn, stack, entity);

            if (foodHandler.isPresent()) {
                InteractionResult response = foodHandler.get().consume(dogIn, stack, entity);
                source.setStackInSlot(i, stack);
                return response;
            }
        }

        return InteractionResult.PASS;
    }

    public static Pair<ItemStack, Integer> findStack(IItemHandler source, Predicate<ItemStack> searchCriteria) {
        for (int i = 0; i < source.getSlots(); i++) {

            ItemStack stack = source.getStackInSlot(i);
            if (searchCriteria.test(stack)) {
                return Pair.of(stack.copy(), i);
            }
        }

        return null;
    }

    public static void transferStacks(IItemHandlerModifiable source, IItemHandler target) {
        for (int i = 0; i < source.getSlots(); i++) {
            ItemStack stack = source.getStackInSlot(i);
            source.setStackInSlot(i, addItem(target, stack));
        }
    }

    public static ItemStack addItem(IItemHandler target, ItemStack remaining) {
        // Try to insert item into all slots
        for (int i = 0; i < target.getSlots(); i++) {
            if (target.isItemValid(i, remaining)) {
                remaining = target.insertItem(i, remaining, false);
            }

            if (remaining.isEmpty()) {
                break;
            }
        }
        return remaining;
    }

    // Same as net.minecraft.inventory.container.Container.calcRedstoneFromInventory but for IItemHandler
    public static int calcRedstoneFromInventory(@Nullable IItemHandler inv) {
        if (inv == null) {
           return 0;
        } else {
           int i = 0;
           float f = 0.0F;

           for (int j = 0; j < inv.getSlots(); ++j) {
              ItemStack itemstack = inv.getStackInSlot(j);
              if (!itemstack.isEmpty()) {
                 f += itemstack.getCount() / (float)Math.min(inv.getSlotLimit(j), itemstack.getMaxStackSize());
                 ++i;
              }
           }

           f = f / inv.getSlots();
           return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
     }
}
