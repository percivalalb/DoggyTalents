package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BedFinderTalent extends TalentInstance {

    public BedFinderTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDogEntity dog) {

    }

    @Override
    public InteractionResult processInteract(AbstractDogEntity dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        if (this.level() > 0) {
            if (!playerIn.hasPassenger(dogIn)) {
                if (playerIn.getItemInHand(handIn).getItem() == Items.BONE && dogIn.canInteract(playerIn)) {

                    if (dogIn.startRiding(playerIn)) {
                        if (!dogIn.level.isClientSide) {
                            dogIn.setOrderedToSit(true);
                        }

                        playerIn.displayClientMessage(Component.translatable("talent.doggytalents.bed_finder.dog_mount", dogIn.getGenderPronoun()), true);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (!dogIn.level.isClientSide) dogIn.stopRiding();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
