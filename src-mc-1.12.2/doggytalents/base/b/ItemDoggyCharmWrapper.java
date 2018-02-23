package doggytalents.base.b;

import doggytalents.item.ItemDoggyCharm;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDoggyCharmWrapper extends ItemDoggyCharm {

	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return true;
        }
        else {
        	IBlockState iblockstate = world.getBlockState(pos);
        	pos = pos.offset(side);
            double yOffset = 0.0D;

            if(side == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block{
            	yOffset = 0.5D;

            if(spawnCreature(world, (double)pos.getX() + 0.5D, (double)pos.getY() + yOffset, (double)pos.getZ() + 0.5D, playerIn) != null && !playerIn.capabilities.isCreativeMode)
                --stack.stackSize;

            return true;
        }
    }
}
