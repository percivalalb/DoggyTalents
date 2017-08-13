package doggytalents.base.b;

import doggytalents.base.IWaterMovement;
import doggytalents.entity.EntityDog;
import net.minecraft.pathfinding.PathNavigateGround;

public class WaterMovementHandler implements IWaterMovement {

	public EntityDog dog;
	private boolean preShouldAvoidWater;
	
	public WaterMovementHandler(EntityDog dogIn) {
		this.dog = dogIn;
	}
	
	@Override
	public void startExecuting() {
		this.preShouldAvoidWater = ((PathNavigateGround)this.dog.getNavigator()).getAvoidsWater();
	      ((PathNavigateGround)this.dog.getNavigator()).setAvoidsWater(false);
	}

	@Override
	public void resetTask() {
        ((PathNavigateGround)this.dog.getNavigator()).setAvoidsWater(this.preShouldAvoidWater);
	}

}
