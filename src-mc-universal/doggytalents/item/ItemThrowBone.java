package doggytalents.item;

import doggytalents.base.ObjectLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends ItemDT {

	public ItemThrowBone() {
		super();
		this.setMaxStackSize(1);
	}
	
	public void setHeadingFromThrower(EntityItem entityItem, Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -ObjectLib.BRIDGE.sin(rotationYawIn * 0.017453292F) * ObjectLib.BRIDGE.cos(rotationPitchIn * 0.017453292F);
        float f1 = -ObjectLib.BRIDGE.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = ObjectLib.BRIDGE.cos(rotationYawIn * 0.017453292F) * ObjectLib.BRIDGE.cos(rotationPitchIn * 0.017453292F);
        this.setThrowableHeading(entityItem, (double)f, (double)f1, (double)f2, velocity, inaccuracy);
        entityItem.motionX += entityThrower.motionX;
        entityItem.motionZ += entityThrower.motionZ;

        if(!entityThrower.onGround)
        	entityItem.motionY += entityThrower.motionY;
    }

    public void setThrowableHeading(EntityItem entityItem, double x, double y, double z, float velocity, float inaccuracy) {
        float f = ObjectLib.BRIDGE.sqrt(x * x + y * y + z * z);
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
        float f1 = ObjectLib.BRIDGE.sqrt(x * x + z * z);
        entityItem.rotationYaw = (float)(ObjectLib.BRIDGE.atan2(x, z) * (180D / Math.PI));
        entityItem.rotationPitch = (float)(ObjectLib.BRIDGE.atan2(y, (double)f1) * (180D / Math.PI));
        entityItem.prevRotationYaw = entityItem.rotationYaw;
        entityItem.prevRotationPitch = entityItem.rotationPitch;
    }
    
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
    }
}
