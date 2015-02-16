package doggytalents.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class EntityAIDogBeg extends EntityAIBase {
	
	private EntityDog theDog;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int field_75384_e;
    private static final String __OBFID = "CL_00001576";

    public EntityAIDogBeg(EntityDog theDog, float minPlayerDistance) {
        this.theDog = theDog;
        this.worldObject = theDog.worldObj;
        this.minPlayerDistance = minPlayerDistance;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theDog, (double)this.minPlayerDistance);
        return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
    }

    @Override
    public boolean continueExecuting() {
        return !this.thePlayer.isEntityAlive() ? false : (this.theDog.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance) ? false : this.field_75384_e > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
    }

    @Override
    public void startExecuting() {
        this.theDog.setBegging(true);
        this.field_75384_e = 40 + this.theDog.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.theDog.setBegging(false);
        this.thePlayer = null;
    }

    @Override
    public void updateTask() {
        this.theDog.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
        --this.field_75384_e;
    }

    private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();
        return stack != null && (this.theDog.isBreedingItem(stack) || DoggyTalentsAPI.BEG_WHITELIST.containsItem(stack) || this.theDog.foodValue(stack) > 0);
    }
}
