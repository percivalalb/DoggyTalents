package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm() {
        super();
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        }
        else {
        	IBlockState iblockstate = world.getBlockState(pos);
        	pos = pos.offset(side);
            double yOffset = 0.0D;

            if (side == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block{
            	yOffset = 0.5D;

            if (spawnCreature(world, (double)pos.getX() + 0.5D, (double)pos.getY() + yOffset, (double)pos.getZ() + 0.5D, player) != null && !player.capabilities.isCreativeMode)
                --stack.stackSize;

            return true;
        }
    }
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, EntityPlayer playerIn) {

        EntityDog dog = new EntityDog(worldIn);

        dog.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.updateEntityAttributes();
        dog.setOwnerId(playerIn.getUniqueID().toString());
        worldIn.spawnEntityInWorld(dog);
        dog.playLivingSound();

        return dog;
    }
        
}
