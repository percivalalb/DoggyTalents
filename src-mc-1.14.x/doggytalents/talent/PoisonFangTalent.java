package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

/**
 * @author ProPercivalalb
 */
public class PoisonFangTalent extends Talent {

	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, PlayerEntity player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getDogHunger() > 30) {
	    			if(!dog.world.isRemote) {
	    				player.clearActivePotions();
	    			}

	    			dog.setDogHunger(dog.getDogHunger() - 30);
	    			return ActionResult.newResult(ActionResultType.SUCCESS, stack);
	    		}
	    	}
	    }
		
	    return ActionResult.newResult(ActionResultType.PASS, stack);
	}
	
	@Override
	public boolean isPostionApplicable(EntityDog dog, EffectInstance potionEffect) {
        if(dog.TALENTS.getLevel(this) >= 3)
        	if(potionEffect.getPotion() == Effects.POISON)
        		return false;
        
        return true;
	}
	
	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.TALENTS.getLevel(this);
		
		if(entity instanceof LivingEntity && level > 0)
			((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.POISON, level * 20, 0));
	    
		return damage;
	}
}
