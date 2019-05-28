package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;

public class EntityAIOwnerHurtTargetDog extends EntityAIOwnerHurtTarget {

	private EntityDog dog;
	
	public EntityAIOwnerHurtTargetDog(EntityDog dogIn) {
		super(dogIn);
		this.dog = dogIn;
	}

	@Override
	public boolean shouldExecute() {
		 return (this.dog.MODE.isMode(EnumMode.AGGRESIVE) || this.dog.MODE.isMode(EnumMode.BERSERKER) || this.dog.MODE.isMode(EnumMode.TACTICAL)) && super.shouldExecute();
	}
}
