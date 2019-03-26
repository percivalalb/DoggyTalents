package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * @author ProPercivalalb
 */
public class PoisonFang extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getDogHunger() > 30) {
	    			player.getActivePotionMap().clear();
	    			dog.setDogHunger(dog.getDogHunger() - 30);
                	return true;
	    		}
	    	}
	    }
		
		return false;
	}
	
	@Override
	public boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) {
        if(dog.TALENTS.getLevel(this) >= 3)
        	return potionEffect.getPotion() == MobEffects.POISON;
        
        return true;
	}
	
	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.TALENTS.getLevel(this);
		
		if(entity instanceof EntityLivingBase && level > 0)
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.POISON, level * 20, 0));
	    
		return damage;
	}
	
	@Override
	public String getKey() {
		return "poisonfang";
	}
}
