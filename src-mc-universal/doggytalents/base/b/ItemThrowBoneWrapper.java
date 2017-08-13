package doggytalents.base.b;

import java.util.List;

import doggytalents.base.ObjectLib;
import doggytalents.item.ItemThrowBone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemThrowBoneWrapper extends ItemThrowBone {

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World worldIn, EntityPlayer playerIn) {
    	if(itemstack.getItemDamage() % 2 == 1) {
    		itemstack.setItemDamage(itemstack.getItemDamage() - 1);
    		playerIn.swingItem();
    	}
    	else {
    		playerIn.swingItem();
    		worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            ItemStack itemstack1 = itemstack;
              
            if (!worldIn.isRemote)
	        {
	        	EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, itemstack.copy());
	            entityitem.setPickupDelay(40);
	            this.setHeadingFromThrower(entityitem, playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.2F, 1.0F);
                worldIn.spawnEntity(entityitem);
	        }
	        
	        if(!playerIn.capabilities.isCreativeMode)
	        	ObjectLib.STACK_UTIL.shrink(itemstack, 1);
    	}
    	return itemstack;
    }
	
	@Override
    public void getSubItems(Item itemIn, CreativeTabs par2CreativeTabs, List subItems) {
		for(int i = 0; i < 4; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
    }
}
