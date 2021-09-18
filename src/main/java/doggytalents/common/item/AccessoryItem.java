package doggytalents.common.item;

import java.util.function.Supplier;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class AccessoryItem extends Item implements IDogItem {

    public Supplier<? extends Accessory> type;

    public AccessoryItem(Supplier<? extends Accessory> type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (dogIn.canInteract(playerIn) && dogIn.addAccessory(this.createInstance(dogIn, playerIn.getItemInHand(handIn), playerIn))) {
            dogIn.consumeItemFromStack(playerIn, playerIn.getItemInHand(handIn));
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public AccessoryInstance createInstance(AbstractDogEntity dogIn, ItemStack stack, PlayerEntity playerIn) {
        return this.type.get().getDefault();
    }
}
