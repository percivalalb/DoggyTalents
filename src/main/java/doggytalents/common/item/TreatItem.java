package doggytalents.common.item;

import doggytalents.api.inferface.IDogItem;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.DogLevel;
import doggytalents.common.lib.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TreatItem extends Item implements IDogItem {

    private final int maxLevel;
    private final DogLevel.Type type;

    public TreatItem(int maxLevel, DogLevel.Type typeIn, Properties properties) {
        super(properties);
        this.maxLevel = maxLevel;
        this.type = typeIn;
    }

    @Override
    public ActionResultType processInteract(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!dogIn.isTamed() || !dogIn.canInteract(playerIn)) {
            return ActionResultType.FAIL;
        }

        DogLevel dogLevel = dogIn.getLevel();

        if (dogIn.getGrowingAge() < 0) {

            if(!worldIn.isRemote) {
                worldIn.setEntityState(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".too_young"));
            }

            return ActionResultType.CONSUME;
        } else if (!dogLevel.canIncrease(this.type)) {

            if(!worldIn.isRemote) {
                worldIn.setEntityState(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".low_level"));
            }

            return ActionResultType.CONSUME;
        }
        else if(dogLevel.getLevel(this.type) < this.maxLevel) {

            if(!playerIn.world.isRemote) {
                if(!playerIn.abilities.isCreativeMode) {
                    playerIn.getHeldItem(handIn).shrink(1);
                }

                dogIn.increaseLevel(this.type);
                dogIn.setHealth(dogIn.getMaxHealth());
                dogIn.getAISit().setSitting(true);
                worldIn.setEntityState(dogIn, Constants.EntityState.WOLF_HEARTS);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".level_up"));
            }

            return ActionResultType.SUCCESS;
        }
        else {

            if(!worldIn.isRemote) {
                worldIn.setEntityState(dogIn, Constants.EntityState.WOLF_SMOKE);
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".max_level"));
            }

            return ActionResultType.CONSUME;
        }
    }
}
