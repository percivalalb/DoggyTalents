package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class RangedAttackerTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("rangedcooldown", 0);
        dog.putObject("rangedattacktype", "");
    }

    @Override
    public void writeAdditional(IDogEntity dog, CompoundNBT tagCompound) {
        int rangedCooldown = dog.getObject("rangedcooldown", Integer.TYPE);
        tagCompound.putInt("rangedcooldown", rangedCooldown);

        String rangedAttackType = tagCompound.getString("rangedattacktype");
        tagCompound.putString("rangedattacktype", rangedAttackType);
    }

    @Override
    public void readAdditional(IDogEntity dog, CompoundNBT tagCompound) {
        dog.putObject("rangedcooldown", tagCompound.getInt("rangedcooldown"));
    }

    @Override
    public ActionResultType onInteract(IDogEntity dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if(stack.isEmpty() && dogIn.canInteract(playerIn)) {
            if(dogIn.getTalentFeature().getLevel(this) > 0 && playerIn.getRidingEntity() == null  && !playerIn.onGround && !dogIn.getHungerFeature().isIncapacicated()) {
                if(!dogIn.world.isRemote) {
                    //TODO RangedAttacker
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }
}
