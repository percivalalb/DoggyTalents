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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

/**
 * @author ProPercivalalb
 */
public class PoisonFangTalent extends Talent {

	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getDogHunger() > 30) {
	    			if(!dog.world.isRemote) {
	    				player.clearActivePotions();
	    			}

	    			dog.setDogHunger(dog.getDogHunger() - 30);
	    			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	    		}
	    	}
	    }
		
	    return ActionResult.newResult(EnumActionResult.PASS, stack);
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
