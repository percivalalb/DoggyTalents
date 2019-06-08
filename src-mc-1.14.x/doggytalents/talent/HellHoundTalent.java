package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class HellHoundTalent extends Talent {

	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.TALENTS.getLevel(this);
		if(level != 0)
            entity.setFire(level);
		return damage;
	}
	
	@Override
	public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
		if(dog.TALENTS.getLevel(this) == 5)
    		if(damageSource.isFireDamage())
    			return false;
		
		return true;
	}
	
	@Override
	public boolean setFire(EntityDog dog, int amount) { 
		return dog.TALENTS.getLevel(this) != 5; 
	}
}
