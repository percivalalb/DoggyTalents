package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author ProPercivalalb
 */
public class PoisonFang extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player) {
		ItemStack stack = player.inventory.getCurrentItem();
		int level = dog.talents.getLevel(this);
		
	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Items.spider_eye  && !player.worldObj.isRemote && dog.getDogHunger() > 30) {
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
        if(dog.talents.getLevel(this) >= 3) {
            int i = potionEffect.getPotionID();

            if (i == Potion.poison.id)
                return false;
        }
        
        return true;
	}
	
	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.talents.getLevel(this);
		
		if(entity instanceof EntityLivingBase && level > 0)
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, level * 20, 0));
	    
		return damage;
	}
	
	@Override
	public String getKey() {
		return "poisonfang";
	}
}
