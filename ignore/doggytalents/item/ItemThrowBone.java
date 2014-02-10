package doggytalents.item;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ItemThrowBone extends Item {
	
	String path_1;
	String path_2;
	Icon droolBone;
	
	public ItemThrowBone(int par1, String par2Str, String par3Str) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabFood);
		this.path_1 = par2Str;
		this.path_2 = par3Str;
	}

	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister) {
		this.iconIndex = par1IconRegister.func_94245_a(path_1);
		this.droolBone = par1IconRegister.func_94245_a(path_2);
	}
	
	public Icon getIconFromDamage(int par1)
    {
		if(par1 == 0) {
	        return this.iconIndex;
		}
		else {
			return this.droolBone;
		}
    }
	
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
    	if(itemstack.getItemDamage() == 1) {
    		itemstack.setItemDamage(0);
    		player.swingItem();
    	}
    	else {
    		player.swingItem();
        	player.worldObj.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            ItemStack itemstack1 = itemstack;
              
              if (!player.worldObj.isRemote)
              {
                  EntityItem entityitem = new EntityItem(player.worldObj, player.posX, (player.posY - 0.30000001192092896D) + (double)player.getEyeHeight(), player.posZ, itemstack1);
                  entityitem.delayBeforeCanPickup = 40;
                  float f = 1.0F;
                  entityitem.motionX = - MathHelper.sin((player.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((player.rotationPitch / 180F) * (float)Math.PI) * f;
                  entityitem.motionZ = MathHelper.cos((player.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((player.rotationPitch / 180F) * (float)Math.PI) * f;
                  entityitem.motionY = - MathHelper.sin((player.rotationPitch / 180F) * (float)Math.PI) * f + 0.1F;
                  f = 0.3F;
                  float f1 = itemRand.nextFloat() * (float)Math.PI * 2.0F;
                  f *= itemRand.nextFloat();
                  entityitem.motionX += Math.cos(f1) * (double)f;
                  entityitem.motionY += (itemRand.nextFloat() - itemRand.nextFloat()) * 0.1F;
                  entityitem.motionZ += Math.sin(f1) * (double)f;
                  player.joinEntityItemWithWorld(entityitem);
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
              }

    	}
        return itemstack;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
    
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
    }
}
