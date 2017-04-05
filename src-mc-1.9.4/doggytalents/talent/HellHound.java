package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class HellHound extends ITalent {

	@Override
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int level = dog.talents.getLevel(this);
		if(level != 0)
            entity.setFire(level);
		return damage;
	}
	
	@Override
	public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
		if(dog.talents.getLevel(this) == 5)
    		if(damageSource.isFireDamage())
    			return false;
		
		return true;
	}
	
	@Override
	public boolean setFire(EntityDog dog, int amount) { 
		return dog.talents.getLevel(this) != 5; 
	}
	
	@Override
	public String getKey() {
		return "hellhound";
	}

}
