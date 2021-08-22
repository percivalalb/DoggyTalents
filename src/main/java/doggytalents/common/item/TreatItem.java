package doggytalents.common.item;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.common.lib.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class TreatItem extends Item implements IDogItem {

    private final int maxLevel;
    private final DogLevel.Type type;

    public TreatItem(int maxLevel, DogLevel.Type typeIn, Properties properties) {
        super(properties);
        this.maxLevel = maxLevel;
        this.type = typeIn;
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!dogIn.isTame() || !dogIn.canInteract(playerIn)) {
            return ActionResultType.FAIL;
        }

        DogLevel dogLevel = dogIn.getLevel();

        if (dogIn.getAge() < 0) {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".too_young"), dogIn.getUUID());
            }

            return ActionResultType.CONSUME;
        } else if (!dogLevel.canIncrease(this.type)) {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".low_level"), dogIn.getUUID());
            }

            return ActionResultType.CONSUME;
        }
        else if (dogLevel.getLevel(this.type) < this.maxLevel) {

            if (!playerIn.level.isClientSide) {
                if (!playerIn.abilities.instabuild) {
                    playerIn.getItemInHand(handIn).shrink(1);
                }

                dogIn.increaseLevel(this.type);
                dogIn.setHealth(dogIn.getMaxHealth());
                dogIn.setOrderedToSit(true);
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_HEARTS);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".level_up"), dogIn.getUUID());
            }

            return ActionResultType.SUCCESS;
        }
        else {

            if (!worldIn.isClientSide) {
                worldIn.broadcastEntityEvent(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".max_level"), dogIn.getUUID());
            }

            return ActionResultType.CONSUME;
        }
    }
}
