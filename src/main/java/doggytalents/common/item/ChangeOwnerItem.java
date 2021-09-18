package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.common.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class ChangeOwnerItem extends Item implements IDogItem {

    public ChangeOwnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!dogIn.isOwnedBy(playerIn)) {

            if (!worldIn.isClientSide) {
                dogIn.tame(playerIn);
                dogIn.getNavigation().stop();
                dogIn.setTarget((LivingEntity) null);
                dogIn.setOrderedToSit(true);
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_HEARTS);

                //TODO playerIn.sendMessage(new TranslationTextComponent(""));
            }

            return ActionResultType.SUCCESS;
        }

        //TODO playerIn.sendMessage(new TranslationTextComponent(""));
        return ActionResultType.FAIL;
    }
}
