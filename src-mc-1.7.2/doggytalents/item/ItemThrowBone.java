package doggytalents.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.api.DoggyTalentsAPI;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends Item {
	
	private IIcon droolBone;
	
	public ItemThrowBone() {
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("doggytalents:throwbone");
		this.droolBone = par1IconRegister.registerIcon("doggytalents:droolThrowBone");
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		switch(meta) {
		case 0: return this.itemIcon;
		case 1: return this.droolBone;
		default: return null;
		}
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
    	if(itemstack.getItemDamage() == 1) {
    		itemstack.setItemDamage(0);
    		player.swingItem();
    	}
    	else {
    		player.swingItem();
        	player.worldObj.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            ItemStack itemstack1 = itemstack;
              
            if (!player.worldObj.isRemote) {
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
