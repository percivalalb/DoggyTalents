package doggytalents.entity.ai;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIDogBeg extends EntityAIBase
{
    private final EntityDog theDog;
    private EntityPlayer thePlayer;
    private final World worldObject;
    private final float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIDogBeg(EntityDog wolf, float minDistance)
    {
        this.theDog = wolf;
        this.worldObject = wolf.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theDog, (double)this.minPlayerDistance);
        return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        return !this.thePlayer.isEntityAlive() ? false : (this.theDog.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance) ? false : this.timeoutCounter > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.theDog.setBegging(true);
        this.timeoutCounter = 40 + this.theDog.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.theDog.setBegging(false);
        this.thePlayer = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        this.theDog.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
        for (EnumHand enumhand : EnumHand.values()) {
            ItemStack itemstack = player.getHeldItem(enumhand);

            if (!itemstack.isEmpty()) {
        
                if(DoggyTalentsAPI.BEG_WHITELIST.containsItem(itemstack))
                	return true;

                if(this.theDog.isBreedingItem(itemstack))
                    return true;
                
                if(this.theDog.foodValue(itemstack) > 0)
                    return true;
            }
        }

        return false;
    }
}