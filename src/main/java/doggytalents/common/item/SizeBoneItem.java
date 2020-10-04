package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
        if (dogIn.getGrowingAge() < 0) {

            if (!playerIn.world.isRemote){
                playerIn.sendMessage(new TranslationTextComponent("treat."+this.type.getName()+".too_young"));
            }

            return ActionResultType.FAIL;
        }
        else {
            if (!playerIn.abilities.isCreativeMode) {
                playerIn.getHeldItem(handIn).shrink(1);
            }

            if (!playerIn.world.isRemote) {
                dogIn.setDogSize(dogIn.getDogSize() + (this.type == Type.BIG ? 1 : -1));
            }
            return ActionResultType.SUCCESS;
        }
    }
}
