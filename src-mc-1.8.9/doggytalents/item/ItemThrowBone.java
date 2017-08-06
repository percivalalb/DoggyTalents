package doggytalents.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends ItemDT {
	
	public ItemThrowBone() {
		this.setMaxStackSize(1);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(item, 1, 0));
        par3List.add(new ItemStack(item, 1, 1));
    }
    
	//TOOD onItemRightClick
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World worldIn, EntityPlayer playerIn) {
    	if(itemstack.getItemDamage() == 1) {
    		itemstack.setItemDamage(0);
    		playerIn.swingItem();
    	}
    	else {
    		playerIn.swingItem();
    		playerIn.worldObj.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            ItemStack itemstack1 = itemstack;
              
            if (!worldIn.isRemote)
	        {
	        	EntityItem entityitem = new EntityItem(playerIn.worldObj, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, itemstack.copy());
	            entityitem.setPickupDelay(40);
	            this.setHeadingFromThrower(entityitem, playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.2F, 1.0F);
                worldIn.spawnEntityInWorld(entityitem);
	        }
	        
	        if (!playerIn.capabilities.isCreativeMode)
	        	--itemstack.stackSize;
    	}
    	return itemstack;
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

    public void setThrowableHeading(EntityItem entityItem, double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt_float((float) (x * x + y * y + z * z));
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
        float f1 = MathHelper.sqrt_float((float) (x * x + z * z));
        entityItem.rotationYaw = (float)(Math.atan2(x, z) * (180D / Math.PI));
        entityItem.rotationPitch = (float)(Math.atan2(y, (double)f1) * (180D / Math.PI));
        entityItem.prevRotationYaw = entityItem.rotationYaw;
        entityItem.prevRotationPitch = entityItem.rotationPitch;
    }
	
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
    }
}
