package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class SizeBoneItem extends Item implements IDogItem {

    private final SizeBoneItem.Type type;

    public static enum Type {
        TINY("tiny_bone"),
        BIG("big_bone");

        String n;

        Type(String n) {
            this.n = n;
        }

        public String getName() {
            return this.n;
        }
    }

    public SizeBoneItem(SizeBoneItem.Type typeIn, Properties properties) {
        super(properties);
        this.type = typeIn;
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (dogIn.getAge() < 0) {

            if (!playerIn.level.isClientSide){
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".too_young"), dogIn.getUUID());
            }

            return ActionResultType.FAIL;
        }
        else {
            if (!playerIn.abilities.instabuild) {
                playerIn.getItemInHand(handIn).shrink(1);
            }

            if (!playerIn.level.isClientSide) {
                dogIn.setDogSize(dogIn.getDogSize() + (this.type == Type.BIG ? 1 : -1));
            }
            return ActionResultType.SUCCESS;
        }
    }
}
