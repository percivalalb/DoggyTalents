package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author ProPercivalalb
 */
public class BedFinderTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        Entity entityRidden = dog.getRidingEntity();

        if(entityRidden instanceof PlayerEntity && !dog.world.isRemote) {

            PlayerEntity player = (PlayerEntity)entityRidden;
            if(player != null && player.getBedLocation(player.dimension) != null) {
                dog.getCoordFeature().setBedPos(player.getBedLocation(player.dimension));
            }
        }
    }


    @Override
    public ActionResultType onInteract(IDogEntity dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        int level = dogIn.getTalentFeature().getLevel(this);
        if(level > 0 && stack.getItem() == Items.BONE && dogIn.canInteract(playerIn)) {
            dogIn.startRiding(playerIn);
            if(!dogIn.world.isRemote) {
                dogIn.getAISit().setSitting(true);
            }
            playerIn.sendStatusMessage(new TranslationTextComponent("talent.doggytalents.bed_finder.dog_mount", dogIn.getGenderFeature().getGenderPronoun()), true);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}