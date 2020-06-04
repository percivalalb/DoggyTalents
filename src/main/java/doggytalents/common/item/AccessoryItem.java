package doggytalents.common.item;

import java.util.function.Supplier;

import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AccessoryItem extends Item implements IDogItem {

    public Supplier<? extends Accessory> type;

    public AccessoryItem(Supplier<? extends Accessory> type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public ActionResultType processInteract(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (dogIn.canInteract(playerIn) && dogIn.addAccessory(this.createInstance(dogIn, playerIn.getHeldItem(handIn), playerIn))) {
            dogIn.consumeItemFromStack(playerIn, playerIn.getHeldItem(handIn));
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public AccessoryInstance createInstance(DogEntity dogIn, ItemStack stack, PlayerEntity playerIn) {
        return this.type.get().getDefault();
    }
}
