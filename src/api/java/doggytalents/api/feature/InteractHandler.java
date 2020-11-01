package doggytalents.api.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class InteractHandler {

    private static final List<IDogItem> HANDLERS = Collections.synchronizedList(new ArrayList<>(4));

    public static void registerHandler(IDogItem handler) {
        HANDLERS.add(handler);
    }

    public static ActionResultType getMatch(@Nullable AbstractDogEntity dogIn, ItemStack stackIn, PlayerEntity playerIn, Hand handIn) {
        if (stackIn.getItem() instanceof IDogItem) {
            IDogItem item = (IDogItem) stackIn.getItem();
            ActionResultType result = item.processInteract(dogIn, dogIn.world, playerIn, handIn);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        for (IDogItem handler : HANDLERS) {
            ActionResultType result = handler.processInteract(dogIn, dogIn.world, playerIn, handIn);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        return ActionResultType.PASS;
    }
}
