package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * @author ProPercivalalb
 */
public class PoisonFang extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.talents.getLevel(this);
		
	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getDogHunger() > 30) {
	    			player.clearActivePotions();
	    			dog.setDogHunger(dog.getDogHunger() - 30);
                	return true;
	    		}
	    	}
	    }
		
		return false;
	}
	
	@Override
	public boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) {
        if(dog.talents.getLevel(this) >= 3)
        	return ObjectLib.BRIDGE.isPosion(potionEffect);
        
        return true;
	}
	
	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.talents.getLevel(this);
		
		if(entity instanceof EntityLivingBase && level > 0)
			ObjectLib.BRIDGE.addPosion(((EntityLivingBase)entity), level * 20, 0);
	    
		return damage;
	}
	
	@Override
	public String getKey() {
		return "poisonfang";
	}
}
