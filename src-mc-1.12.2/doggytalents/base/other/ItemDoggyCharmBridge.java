package doggytalents.base.other;

import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemDoggyCharm;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ItemDoggyCharmBridge extends ItemDoggyCharm {
	
    public ItemDoggyCharmBridge() {
        super();
    }
    
    public EnumActionResult onItemUseGENERAL(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand handIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(handIn);
    
    	if (worldIn.isRemote)
            return EnumActionResult.SUCCESS;
        else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);


            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, playerIn);

            if (entity != null)
                if(!playerIn.capabilities.isCreativeMode)
                	ObjectLib.STACK_UTIL.shrink(stack, 1);
           

            return EnumActionResult.SUCCESS;
        }
    }
    
    public ActionResult<ItemStack> onItemRightClickGENERAL(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
    	
        if(worldIn.isRemote)
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        else {
            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn)) {
                    Entity entity = this.spawnCreature(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, playerIn);

                    if (entity == null)
                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
                    else {
                        if (!playerIn.capabilities.isCreativeMode)
                        	ObjectLib.STACK_UTIL.shrink(itemStackIn, 1);
      

                        playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
                else
                    return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            }
            else
                return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
    }     
}
