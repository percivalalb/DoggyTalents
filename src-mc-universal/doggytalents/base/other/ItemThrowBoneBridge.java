package doggytalents.base.other;

import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.item.ItemThrowBone;
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
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ItemThrowBoneBridge extends ItemThrowBone {

	public ItemThrowBoneBridge() {
		super();
	}
	
	public ActionResult<ItemStack> onItemRightClickGENERAL(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		
		if(itemStackIn.getItemDamage() == 1) {
    		itemStackIn.setItemDamage(0);
    		playerIn.swingArm(handIn);
    		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    	}
		else {
	
	        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	
	        if (!worldIn.isRemote)
	        {
	        	EntityItem entityitem = new EntityItem(playerIn.world, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, itemStackIn.copy());
	            entityitem.setPickupDelay(40);
	            this.setHeadingFromThrower(entityitem, playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.2F, 1.0F);
                worldIn.spawnEntity(entityitem);
	        }
	        
	        if (!playerIn.capabilities.isCreativeMode)
	        	ObjectLib.STACK_UTIL.shrink(itemStackIn, 1);

	        playerIn.addStat(StatList.getObjectUseStats(this));
	        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
}
