package doggytalents.item;

import java.util.List;

import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends ItemDT {

	public ItemThrowBone() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if(itemStackIn.getItemDamage() == 1) {
    		itemStackIn.setItemDamage(0);
    		playerIn.swingArm(hand);
    		  return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    	}
		else {
			
	        if (!playerIn.capabilities.isCreativeMode)
	        {
	            --itemStackIn.stackSize;
	        }
	
	        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	
	        if (!worldIn.isRemote)
	        {
	        	EntityItem entityitem = new EntityItem(playerIn.worldObj, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, itemStackIn);
	            entityitem.setPickupDelay(40);
                float f = 1.0F;
                entityitem.motionX = - MathHelper.sin((playerIn.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((playerIn.rotationPitch / 180F) * (float)Math.PI) * f;
                entityitem.motionZ = MathHelper.cos((playerIn.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((playerIn.rotationPitch / 180F) * (float)Math.PI) * f;
                entityitem.motionY = - MathHelper.sin((playerIn.rotationPitch / 180F) * (float)Math.PI) * f + 0.1F;
                f = 0.3F;
                float f1 = itemRand.nextFloat() * (float)Math.PI * 2.0F;
                f *= itemRand.nextFloat();
                entityitem.motionX += Math.cos(f1) * (double)f;
                entityitem.motionY += (itemRand.nextFloat() - itemRand.nextFloat()) * 0.1F;
                entityitem.motionZ += Math.sin(f1) * (double)f;
                worldIn.spawnEntityInWorld(entityitem);

                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
	        }
	
	        playerIn.addStat(StatList.getObjectUseStats(this));
	        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(item, 1, 0));
        par3List.add(new ItemStack(item, 1, 1));
    }
    
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
    }
}
