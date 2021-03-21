package doggytalents.common.item;

import java.util.List;

import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.lib.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class DogShearsItem extends Item implements IDogItem {

    private static DataKey<Integer> COOLDOWN = DataKey.make();

    public DogShearsItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        List<AccessoryInstance> accessories = dogIn.getAccessories();
        if (accessories.isEmpty()) {
            if (!dogIn.isTamed()) {
                return ActionResultType.CONSUME;
            }

            if (!worldIn.isRemote) {
                int cooldownLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;

                if (cooldownLeft <= 0) {
                    worldIn.setEntityState(dogIn, Constants.EntityState.WOLF_SMOKE);
                    dogIn.untame();
                }
            }

            return ActionResultType.SUCCESS;
        }

        if (!worldIn.isRemote) {
            for (AccessoryInstance inst : accessories) {
                ItemStack returnItem = inst.getReturnItem();
                dogIn.entityDropItem(returnItem, 1);
            }

            dogIn.removeAccessories();
            dogIn.setData(COOLDOWN, dogIn.ticksExisted + 40);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.SUCCESS;
    }

}
