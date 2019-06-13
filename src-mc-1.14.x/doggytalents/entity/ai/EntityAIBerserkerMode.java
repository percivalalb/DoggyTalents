package doggytalents.entity.ai;

import java.util.function.Predicate;

import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class EntityAIBerserkerMode<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

	private EntityDog dog;
	
	public EntityAIBerserkerMode(EntityDog dogIn, Class<T> classTarget, boolean checkSight) {
		super(dogIn, classTarget, checkSight, false);
		this.dog = dogIn;
	}
	
	public EntityAIBerserkerMode(EntityDog dogIn, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, Predicate<LivingEntity> targetSelector) {
		super(dogIn, classTarget, chance, checkSight, onlyNearby, targetSelector);
		this.dog = dogIn;
	}

	@Override
	public boolean shouldExecute() {
		return this.dog.MODE.isMode(EnumMode.BERSERKER) && super.shouldExecute();
	}
}
