package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class RangedAttackerTalent extends Talent {
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("rangedcooldown", 0);
        dog.objects.put("rangedattacktype", "");
    }
    
    @Override
    public void writeAdditional(EntityDog dog, CompoundNBT tagCompound) {
        int rangedCooldown = (Integer)dog.objects.get("rangedcooldown");
        tagCompound.putInt("rangedcooldown", rangedCooldown);
        
        String rangedAttackType = tagCompound.getString("rangedattacktype");
        tagCompound.putString("rangedattacktype", rangedAttackType);
    }
    
    @Override
    public void readAdditional(EntityDog dog, CompoundNBT tagCompound) {
        dog.objects.put("rangedcooldown", tagCompound.getInt("rangedcooldown"));
    }
    
    @Override
    public ActionResultType onInteract(EntityDog dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        
        if(stack.isEmpty() && dogIn.canInteract(playerIn)) {
            if(dogIn.TALENTS.getLevel(this) > 0 && playerIn.getRidingEntity() == null  && !playerIn.onGround && !dogIn.isIncapacicated()) {
                if(!dogIn.world.isRemote) {
                    //TODO RangedAttacker
                }
                return ActionResultType.SUCCESS;
            }
        }
        
        return ActionResultType.PASS;
    }
}
