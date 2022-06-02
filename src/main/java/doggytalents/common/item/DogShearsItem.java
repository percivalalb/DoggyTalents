package doggytalents.common.item;

import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.lib.Constants;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class DogShearsItem extends Item implements IDogItem {

    private static DataKey<Integer> COOLDOWN = DataKey.make();

    public DogShearsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult processInteract(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        if (dogIn.isOwnedBy(playerIn)) {
            List<AccessoryInstance> accessories = dogIn.getAccessories();
            if (accessories.isEmpty()) {
                if (!dogIn.isTame()) {
                    return InteractionResult.CONSUME;
                }

                if (!worldIn.isClientSide) {
                    int cooldownLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.tickCount) - dogIn.tickCount;

                    if (cooldownLeft <= 0) {
                        worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                        dogIn.untame();
                    }
                }

                return InteractionResult.SUCCESS;
            }

            if (!worldIn.isClientSide) {
                for (AccessoryInstance inst : accessories) {
                    ItemStack returnItem = inst.getReturnItem();
                    dogIn.spawnAtLocation(returnItem, 1);
                }

                dogIn.removeAccessories();
                dogIn.setData(COOLDOWN, dogIn.tickCount + 40);

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

}
