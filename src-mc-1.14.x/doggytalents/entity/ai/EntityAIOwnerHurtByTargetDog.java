package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;

public class EntityAIOwnerHurtByTargetDog extends OwnerHurtByTargetGoal {

	private EntityDog dog;
	
	public EntityAIOwnerHurtByTargetDog(EntityDog dogIn) {
		super(dogIn);
		this.dog = dogIn;
	}

	@Override
	public boolean shouldExecute() {
		 return (this.dog.MODE.isMode(EnumMode.AGGRESIVE) || this.dog.MODE.isMode(EnumMode.BERSERKER)) && super.shouldExecute();
	}
}
