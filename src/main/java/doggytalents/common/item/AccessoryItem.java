package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class AccessoryItem extends Item implements IDogItem {

    public Supplier<? extends Accessory> type;

    public AccessoryItem(Supplier<? extends Accessory> type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult processInteract(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        if (dogIn.canInteract(playerIn) && dogIn.addAccessory(this.createInstance(dogIn, playerIn.getItemInHand(handIn), playerIn))) {
            dogIn.consumeItemFromStack(playerIn, playerIn.getItemInHand(handIn));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public AccessoryInstance createInstance(AbstractDog dogIn, ItemStack stack, Player playerIn) {
        return this.type.get().getDefault();
    }
}
