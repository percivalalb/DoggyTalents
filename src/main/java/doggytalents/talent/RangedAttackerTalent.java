package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public class RangedAttackerTalent extends Talent {
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("rangedcooldown", 0);
        dog.objects.put("rangedattacktype", "");
    }
    
    @Override
    public void writeAdditional(EntityDog dog, NBTTagCompound tagCompound) {
        int rangedCooldown = (Integer)dog.objects.get("rangedcooldown");
        tagCompound.setInteger("rangedcooldown", rangedCooldown);
        
        String rangedAttackType = tagCompound.getString("rangedattacktype");
        tagCompound.setString("rangedattacktype", rangedAttackType);
    }
    
    @Override
    public void readAdditional(EntityDog dog, NBTTagCompound tagCompound) {
        dog.objects.put("rangedcooldown", tagCompound.getInteger("rangedcooldown"));
    }
    
    @Override
    public EnumActionResult onInteract(EntityDog dog, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.isEmpty() && dog.canInteract(player)) {
            if(dog.TALENTS.getLevel(this) > 0 && player.getRidingEntity() == null  && !player.onGround && !dog.isIncapacicated()) {
                if(!dog.world.isRemote) {
                    //TODO RangedAttacker
                }
                return EnumActionResult.SUCCESS;
            }
        }
        
        return EnumActionResult.PASS;
    }
}
