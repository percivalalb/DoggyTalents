package doggytalents.item;

import doggytalents.api.inferface.IDogItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemTinyBone extends ItemDT implements IDogItem {

    public ItemTinyBone() {
        super();
    }
    
    @Override
    public EnumActionResult onInteractWithDog(EntityDog dogIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(dogIn.getGrowingAge() >= 0) {
            if(!playerIn.isCreative())
                playerIn.getHeldItem(handIn).shrink(1);

            if(!playerIn.world.isRemote) {
                dogIn.setDogSize(dogIn.getDogSize() - 1);
            }
            return EnumActionResult.SUCCESS;
        }
        else {
            if(!playerIn.world.isRemote){
                playerIn.sendMessage(new TextComponentTranslation("treat.tiny_bone.too_young"));
            }
            return EnumActionResult.FAIL;
        }    
    }
}