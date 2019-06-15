package doggytalents.item;

import doggytalents.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends ItemDT {
	
	public enum Type {
		DRY,
		WET
	}
	
	public Type type;
	
	public ItemThrowBone() {
		this(Type.DRY);
	}
	
	public ItemThrowBone(Type type) {
		super();
		this.type = type;
	}
	
	public void setHeadingFromThrower(EntityItem entityItem, Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.setThrowableHeading(entityItem, (double)f, (double)f1, (double)f2, velocity, inaccuracy);
        entityItem.motionX += entityThrower.motionX;
        entityItem.motionZ += entityThrower.motionZ;

        if(!entityThrower.onGround)
        	entityItem.motionY += entityThrower.motionY;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(this.type == Type.WET) {
			if(itemStackIn.getItem() == ModItems.THROW_BONE_WET)
				itemStackIn = new ItemStack(ModItems.THROW_BONE);
			else if(itemStackIn.getItem() == ModItems.THROW_STICK_WET)
				itemStackIn = new ItemStack(ModItems.THROW_STICK);

    		playerIn.swingArm(handIn);
    		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    	}
		else {
	
	        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	
	        if(!worldIn.isRemote) {
	        	ItemStack stack = itemStackIn.copy();
	        	stack.stackSize = 1;
	        	EntityItem entityitem = new EntityItem(playerIn.world, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, stack);
	            entityitem.setPickupDelay(40);
	            this.setHeadingFromThrower(entityitem, playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.2F, 1.0F);
                worldIn.spawnEntity(entityitem);
	        }
	        
	        if(!playerIn.capabilities.isCreativeMode)
	        	itemStackIn.stackSize--;

	        playerIn.addStat(StatList.getObjectUseStats(this));
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}

    public void setThrowableHeading(EntityItem entityItem, double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.itemRand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.itemRand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.itemRand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        entityItem.motionX = x;
        entityItem.motionY = y;
        entityItem.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        entityItem.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        entityItem.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        entityItem.prevRotationYaw = entityItem.rotationYaw;
        entityItem.prevRotationPitch = entityItem.rotationPitch;
    }
}
