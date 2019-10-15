package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ProPercivalalb
 */
public class BedFinderTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        Entity entityRidden = dog.getRidingEntity();

        if(entityRidden instanceof EntityPlayer && !dog.world.isRemote) {

            EntityPlayer player = (EntityPlayer)entityRidden;
            if(player != null && player.getBedLocation(player.dimension) != null) {
                dog.getCoordFeature().setBedPos(player.getBedLocation(player.dimension));
            }
        }
    }


    @Override
    public EnumActionResult onInteract(IDogEntity dog, EntityPlayer player, EnumHand hand) {
        int level = dog.getTalentFeature().getLevel(this);
        ItemStack stack = player.getHeldItem(hand);
        if(level > 0 && stack.getItem() == Items.BONE && dog.canInteract(player)) {
            dog.startRiding(player);
            if(!dog.world.isRemote) {
                dog.getAISit().setSitting(true);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}