package doggytalents.item;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm(int var1, String par2Str) {
        super(var1, par2Str);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabFood);
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par3World.isRemote) {
            return true;
        }
        else {
            int var11 = par3World.getBlockId(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double var12 = 0.0D;

            if (par7 == 1 && Block.blocksList[var11] != null && Block.blocksList[var11].getRenderType() == 11) {
                var12 = 0.5D;
            }

            if (spawnCreature(par3World, (double)par4 + 0.5D, (double)par5 + var12, (double)par6 + 0.5D, par2EntityPlayer) != null && !par2EntityPlayer.capabilities.isCreativeMode) {
                --par1ItemStack.stackSize;
            }

            return true;
        }
    }
    
    public static Entity spawnCreature(World par0World, double par2, double par4, double par6, EntityPlayer par7EntityPlayer) {
        EntityDTDoggy var8 = null;

        for (int var9 = 0; var9 < 1; ++var9) {
            var8 = new EntityDTDoggy(par0World);

            if (var8 != null && var8 instanceof EntityLiving) {
                EntityDTDoggy var10 = (EntityDTDoggy)var8;
                var8.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                var10.rotationYawHead = var10.rotationYaw;
                var10.renderYawOffset = var10.rotationYaw;
                var10.setTamed(true);
                var10.setOwner(par7EntityPlayer.username);
                var10.ageCheck = false;
                par0World.spawnEntityInWorld(var8);
                var10.playLivingSound();
            }
        }

        return var8;
    }
        
}
