package doggytalents.entity.ai;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIDogBeg extends EntityAIBase {
	
    private final EntityDog dog;
    private EntityPlayer player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIDogBeg(EntityDog dog, float minDistance) {
        this.dog = dog;
        this.world = dog.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayerToEntity(this.dog, (double)this.minPlayerDistance);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(!this.player.isEntityAlive())
            return false;
        else if(this.dog.getDistanceSqToEntity(this.player) > (double)(this.minPlayerDistance * this.minPlayerDistance))
            return false;
        else
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public void startExecuting() {
        this.dog.setBegging(true);
        this.timeoutCounter = 40 + this.dog.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.dog.setBegging(false);
        this.player = null;
    }

    @Override
    public void updateTask() {
        this.dog.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double)this.player.getEyeHeight(), this.player.posZ, 10.0F, (float)this.dog.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    private boolean hasTemptationItemInHand(EntityPlayer player) {
        for(EnumHand enumhand : EnumHand.values()) {
            ItemStack itemstack = player.getHeldItem(enumhand);

            if(this.dog.isTamed() && DoggyTalentsAPI.BEG_WHITELIST.containsItem(itemstack))
            	return true;

            if(this.dog.isBreedingItem(itemstack))
                return true;
            
            if(this.dog.foodValue(itemstack) > 0)
                return true;
        }

        return false;
    }
}