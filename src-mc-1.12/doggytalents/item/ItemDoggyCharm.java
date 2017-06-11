package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
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
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand handIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
                if (!playerIn.capabilities.isCreativeMode)
                   	stack.shrink(1);
           

            return EnumActionResult.SUCCESS;
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
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
                        	itemStackIn.shrink(1);
      

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
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, EntityPlayer playerIn) {

        EntityDog dog = new EntityDog(worldIn);

        dog.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.updateEntityAttributes();
        dog.setOwnerId(playerIn.getUniqueID());
        worldIn.spawnEntity(dog);
        dog.playLivingSound();

        return dog;
    }
        
}
