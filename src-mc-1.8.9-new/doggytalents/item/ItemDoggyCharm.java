package doggytalents.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm() {
        super();
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
        	IBlockState blockstate = worldIn.getBlockState(pos);
        	pos = pos.offset(side);
            double yOffset = 0.0D;

            if (side == EnumFacing.UP && blockstate.getBlock().getRenderType() == 11)
            	yOffset = 0.5D;

            if (spawnCreature(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + yOffset, (double)pos.getZ() + 0.5D, player) != null && !player.capabilities.isCreativeMode)
                --stack.stackSize;

            return true;
        }
    }
    
    public static Entity spawnCreature(World par0World, double par2, double par4, double par6, EntityPlayer par7EntityPlayer) {
    	EntityDog var8 = null;

        for (int var9 = 0; var9 < 1; ++var9) {
            var8 = new EntityDog(par0World);

            if (var8 != null && var8 instanceof EntityLiving) {
            	EntityDog var10 = (EntityDog)var8;
                var8.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                var10.rotationYawHead = var10.rotationYaw;
                var10.renderYawOffset = var10.rotationYaw;
                var10.setTamed(true);
                var10.updateEntityAttributes();
                var10.setOwnerId(par7EntityPlayer.getUniqueID().toString());
                par0World.spawnEntityInWorld(var8);
                var10.playLivingSound();
            }
        }

        return var8;
    }
        
}
