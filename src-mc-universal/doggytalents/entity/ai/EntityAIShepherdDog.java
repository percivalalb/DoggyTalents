package doggytalents.entity.ai;

import com.google.common.base.Predicate;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

/**
 * @author ProPercivalalb
 */
public class EntityAIShepherdDog extends EntityAINearestAttackableTarget {

	public EntityDog dog;
	
	public EntityAIShepherdDog(EntityDog dog, Class target, int targetChance, boolean shouldCheckSight) {
		super(dog, target, targetChance, shouldCheckSight, true, (Predicate)null);
		this.dog = dog;
	}

	@Override
	public boolean shouldExecute() {
		int order = dog.masterOrder();
        return order == 3 && this.dog.mode.isMode(EnumMode.DOCILE) && this.dog.isTamed() && !this.dog.isBeingRidden() && this.dog.talents.getLevel("shepherddog") > 0 && super.shouldExecute();
    }
	
	@Override
	protected double getTargetDistance() {
	    return 24.0D;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		int order = dog.masterOrder();
        return order == 3 && super.shouldContinueExecuting() && !this.dog.isBeingRidden();
	}
}
