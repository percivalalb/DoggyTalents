package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ProPercivalalb
 */
public class PoisonFangTalent extends Talent {

    @Override
    public EnumActionResult onInteract(EntityDog dog, EntityPlayer player, EnumHand hand) {
        int level = dog.TALENTS.getLevel(this);
        
        if (dog.isTamed()) {
            
            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getDogHunger() > 30) {
                if(!dog.world.isRemote) {
                    player.clearActivePotions();
                }

                dog.setDogHunger(dog.getDogHunger() - 30);
                return EnumActionResult.SUCCESS;
            }
        }
        
        return EnumActionResult.PASS;
    }
    
    @Override
    public boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) {
        if(dog.TALENTS.getLevel(this) >= 3)
            if(potionEffect.getPotion() == MobEffects.POISON)
                return false;
        
        return true;
    }
    
    @Override
    public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
        int level = dog.TALENTS.getLevel(this);
        
        if(entity instanceof EntityLivingBase && level > 0)
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.POISON, level * 20, 0));
        
        return damage;
    }
}
