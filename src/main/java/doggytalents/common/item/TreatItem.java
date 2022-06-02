package doggytalents.common.item;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogItem;
import doggytalents.common.lib.Constants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class TreatItem extends Item implements IDogItem {

    private final int maxLevel;
    private final DogLevel.Type type;

    public TreatItem(int maxLevel, DogLevel.Type typeIn, Properties properties) {
        super(properties);
        this.maxLevel = maxLevel;
        this.type = typeIn;
    }

    @Override
    public InteractionResult processInteract(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!dogIn.isTame() || !dogIn.canInteract(playerIn)) {
            return InteractionResult.FAIL;
        }

        DogLevel dogLevel = dogIn.getDogLevel();

        if (dogIn.getAge() < 0) {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslatableComponent("treat."+this.type.getName()+".too_young"), dogIn.getUUID());
            }

            return InteractionResult.CONSUME;
        } else if (!dogLevel.canIncrease(this.type)) {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslatableComponent("treat."+this.type.getName()+".low_level"), dogIn.getUUID());
            }

            return InteractionResult.CONSUME;
        }
        else if (dogLevel.getLevel(this.type) < this.maxLevel) {

            if (!playerIn.level.isClientSide) {
                if (!playerIn.getAbilities().instabuild) {
                    playerIn.getItemInHand(handIn).shrink(1);
                }

                dogIn.increaseLevel(this.type);
                dogIn.setHealth(dogIn.getMaxHealth());
                dogIn.setOrderedToSit(true);
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_HEARTS);
                playerIn.sendMessage(new TranslatableComponent("treat."+this.type.getName()+".level_up"), dogIn.getUUID());
            }

            return InteractionResult.SUCCESS;
        }
        else {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslatableComponent("treat."+this.type.getName()+".max_level"), dogIn.getUUID());
            }

            return InteractionResult.CONSUME;
        }
    }
}
