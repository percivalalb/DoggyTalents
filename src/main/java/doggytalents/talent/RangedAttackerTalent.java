package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public class RangedAttackerTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("rangedcooldown", 0);
        dog.putObject("rangedattacktype", "");
    }

    @Override
    public void writeAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        int rangedCooldown = dog.getObject("rangedcooldown", Integer.TYPE);
        tagCompound.setInteger("rangedcooldown", rangedCooldown);

        String rangedAttackType = tagCompound.getString("rangedattacktype");
        tagCompound.setString("rangedattacktype", rangedAttackType);
    }

    @Override
    public void readAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        dog.putObject("rangedcooldown", tagCompound.getInteger("rangedcooldown"));
    }

    @Override
    public EnumActionResult onInteract(IDogEntity dog, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.isEmpty() && dog.canInteract(player)) {
            if(dog.getTalentFeature().getLevel(this) > 0 && player.getRidingEntity() == null  && !player.onGround && !dog.getHungerFeature().isIncapacicated()) {
                if(!dog.world.isRemote) {
                    //TODO RangedAttacker
                }
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
