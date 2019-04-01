package doggytalents.entity.ai;

import com.google.common.base.Predicate;

import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityAnimal;

/**
 * @author ProPercivalalb
 */
public class EntityAIShepherdDog extends EntityAINearestAttackableTarget<EntityAnimal> {

	public EntityDog dog;
	
	public EntityAIShepherdDog(EntityDog dog, int targetChance, boolean shouldCheckSight) {
		super(dog, EntityAnimal.class, targetChance, shouldCheckSight, true, (Predicate<EntityAnimal>)null);
		this.dog = dog;
	}

	@Override
	public boolean shouldExecute() {
		int order = dog.masterOrder();
        return order == 3 && this.dog.MODE.isMode(EnumMode.DOCILE) && this.dog.isTamed() && !this.dog.isBeingRidden() && this.dog.TALENTS.getLevel("shepherddog") > 0 && super.shouldExecute();
    }
	
	@Override
	protected double getTargetDistance() {
	    return 24.0D;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		int order = this.dog.masterOrder();
        return order == 3 && super.shouldContinueExecuting() && !this.dog.isBeingRidden();
	}
}
