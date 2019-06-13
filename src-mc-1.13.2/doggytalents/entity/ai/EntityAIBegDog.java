package doggytalents.entity.ai;

import doggytalents.ModTags;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IWorldReaderBase;

public class EntityAIBegDog extends EntityAIBase {
	
	private final EntityDog dog;
	private final IWorldReaderBase world;
	private final float minPlayerDistance;
	
	private EntityPlayer player;
	private int timeoutCounter;

	public EntityAIBegDog(EntityDog dog, float minDistance) {
		this.dog = dog;
		this.world = dog.world;
		this.minPlayerDistance = minDistance;
		this.setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		this.player = this.world.getClosestPlayerToEntity(this.dog, (double) this.minPlayerDistance);
		return this.player == null ? false : this.hasTemptationItemInHand(this.player);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(!this.player.isAlive()) {
			return false;
		} else if(this.dog.getDistanceSq(this.player) > (double) (this.minPlayerDistance * this.minPlayerDistance)) {
			return false;
		} else {
			return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
		}
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
	public void tick() {
		this.dog.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double) this.player.getEyeHeight(), this.player.posZ, 10.0F, (float) this.dog.getVerticalFaceSpeed());
		--this.timeoutCounter;
	}

	/**
	 * Gets if the Player has the Bone in the hand.
	 */
	private boolean hasTemptationItemInHand(EntityPlayer player) {
		for (EnumHand enumhand : EnumHand.values()) {
			ItemStack itemstack = player.getHeldItem(enumhand);
			if(itemstack.getItem().isIn(this.dog.isTamed() ? ModTags.getTag(ModTags.BEG_ITEMS_TAMED) : ModTags.getTag(ModTags.BEG_ITEMS_UNTAMED)))
            	return true;

            if(this.dog.foodValue(itemstack) > 0)
                return true;
			
			if(this.dog.isBreedingItem(itemstack))
                return true;
       
		}

		return false;
	}
}