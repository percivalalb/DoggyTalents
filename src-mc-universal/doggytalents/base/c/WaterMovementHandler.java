package doggytalents.base.c;

import doggytalents.base.IWaterMovement;
import doggytalents.entity.EntityDog;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class WaterMovementHandler implements IWaterMovement {

	public EntityDog dog;
    private float oldWaterCost;
	
	public WaterMovementHandler(EntityDog dogIn) {
		this.dog = dogIn;
	}
	
	@Override
	public void startExecuting() {
        this.oldWaterCost = this.dog.getPathPriority(PathNodeType.WATER);
        this.dog.setPathPriority(PathNodeType.WATER, 0.0F);
		
	}

	@Override
	public void resetTask() {
        this.dog.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
	}
}
