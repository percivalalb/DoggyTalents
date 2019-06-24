package doggytalents.entity.ai;

import doggytalents.api.inferface.IWaterMovement;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;

public class WaterMovementHandler implements IWaterMovement {

    public MobEntity dog;
    private float oldWaterCost;
    private final float waterCost;
    
    public WaterMovementHandler(MobEntity dogIn) {
        this(dogIn, 0.0F);
    }
    
    public WaterMovementHandler(MobEntity dogIn, float waterCostIn) {
        this.dog = dogIn;
        this.waterCost = waterCostIn;
    }
    
    @Override
    public void startExecuting() {
        this.oldWaterCost = this.dog.getPathPriority(PathNodeType.WATER);
        this.dog.setPathPriority(PathNodeType.WATER, this.waterCost);
    }

    @Override
    public void resetTask() {
        this.dog.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
}
