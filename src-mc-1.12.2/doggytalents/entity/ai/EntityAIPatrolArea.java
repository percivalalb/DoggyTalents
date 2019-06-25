/**package doggytalents.entity.ai;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.ModeFeature.EnumMode;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIPatrolArea extends EntityAIBase {

    public EntityDog dog;
    public int index;
    private int timeToRecalcPath;
    
    public EntityAIPatrolArea(EntityDog dogIn) {
        this.dog = dogIn;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.dog.MODE.isMode(EnumMode.PATROL) && this.dog.patrolOutline.size() > 1;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dog.MODE.isMode(EnumMode.PATROL) && this.dog.patrolOutline.size() > 1 && !this.dog.isSitting();
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.index = 0;
    }
    
    @Override
    public void resetTask() {
        this.dog.getNavigator().clearPathEntity();
    }
    
    @Override
    public void updateTask() {
        DoggyTalents.LOGGER.info("Update" + this.index);
        BlockPos pos = this.dog.patrolOutline.get(this.index);
        
        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if(!this.dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0F)) {
                    this.index = (this.index + 1) % this.dog.patrolOutline.size();
    
                }
                else if(this.dog.getNavigator().getPath() == null) {
                    this.index = (this.index + 1) % this.dog.patrolOutline.size();
                }
        
            }
        }
    }
    
}**/
