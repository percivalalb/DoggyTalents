package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class EntityAIFollowOwnerDog extends FollowOwnerGoal {
    private final EntityDog dog;

	// If further away that minDistIn task will execute 
	// If closer that maxDistIn task will end
    
    public EntityAIFollowOwnerDog(EntityDog dogIn, double followSpeedIn, float minDistIn, float maxDistIn) {
    	super(dogIn, followSpeedIn, minDistIn, maxDistIn);
        this.dog = dogIn;
       
    }

    @Override
    public boolean shouldExecute() {
    	return !this.dog.canWander() && super.shouldExecute();
    }
 
}