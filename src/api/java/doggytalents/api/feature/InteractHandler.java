package doggytalents.api.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class InteractHandler {

    private static final List<IDogItem> HANDLERS = Collections.synchronizedList(new ArrayList<>(4));

    public static void registerHandler(IDogItem handler) {
        HANDLERS.add(handler);
    }

    public static InteractionResult getMatch(@Nullable AbstractDog dogIn, ItemStack stackIn, Player playerIn, InteractionHand handIn) {
        if (stackIn.getItem() instanceof IDogItem) {
            IDogItem item = (IDogItem) stackIn.getItem();
            InteractionResult result = item.processInteract(dogIn, dogIn.level, playerIn, handIn);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        for (IDogItem handler : HANDLERS) {
            InteractionResult result = handler.processInteract(dogIn, dogIn.level, playerIn, handIn);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    }
}
