package doggytalents.item;

import doggytalents.DoggyTalentsMod;
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
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm(int id, String iconPath) {
        super(id, iconPath);
        this.setMaxStackSize(1);
        this.setCreativeTab(DoggyTalentsMod.creativeTab);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        }
        else {
            int blockId = world.getBlockId(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double var12 = 0.0D;

            if (side == 1 && Block.blocksList[blockId] != null && Block.blocksList[blockId].getRenderType() == 11) {
                var12 = 0.5D;
            }

            if (spawnCreature(world, (double)x + 0.5D, (double)y + var12, (double)z + 0.5D, player) != null && !player.capabilities.isCreativeMode) {
                --stack.stackSize;
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
                par0World.spawnEntityInWorld(var8);
                var10.playLivingSound();
            }
        }

        return var8;
    }
        
}
