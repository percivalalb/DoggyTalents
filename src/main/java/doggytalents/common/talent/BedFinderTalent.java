package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BedFinderTalent extends TalentInstance {

    public BedFinderTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDogEntity dog) {

    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (this.level() > 0) {
            if (!playerIn.isPassenger(dogIn)) {
                if (playerIn.getHeldItem(handIn).getItem() == Items.BONE && dogIn.canInteract(playerIn)) {

                    if (dogIn.startRiding(playerIn)) {
                        if (!dogIn.world.isRemote) {
                            dogIn.getAISit().setSitting(true);
                        }

                        playerIn.sendStatusMessage(new TranslationTextComponent("talent.doggytalents.bed_finder.dog_mount", dogIn.getGenderPronoun()), true);
                    }

                    return ActionResultType.CONSUME;
                }
            } else {
                dogIn.stopRiding();
                return ActionResultType.SUCCESS;
            }

        }
        return ActionResultType.PASS;
    }
}
