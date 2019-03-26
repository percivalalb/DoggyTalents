package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Item.Properties;
import net.minecraft.stats.StatList;
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
public class ItemDoggyCharm extends Item {
	
	public ItemDoggyCharm(Properties properties) {
		super(properties);
    }
    
    @Override
	public EnumActionResult onItemUse(ItemUseContext context) {
    	EntityPlayer player = context.getPlayer();
		ItemStack stack = context.getItem();
		BlockPos pos = context.getPos();
		EnumFacing facing = context.getFace();
		World world = context.getWorld();
		
    	if (world.isRemote)
            return EnumActionResult.SUCCESS;
        else if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = world.getBlockState(pos);


            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(world, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, player);

            if (entity != null)
                if(!player.isCreative())
                	stack.shrink(1);
           

            return EnumActionResult.SUCCESS;
        }
    }
    
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
    	
        if(worldIn.isRemote)
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        else {
            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

            if (raytraceresult != null && raytraceresult.type == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                
                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockFlowingFluid))
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn)) {
                    Entity entity = this.spawnCreature(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, playerIn);

                    if (entity == null)
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
                    else {
                        if (!playerIn.isCreative())
                        	itemStackIn.shrink(1);
      

                        playerIn.addStat(StatList.ITEM_USED.get(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
                else
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
            }
            else
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        }
    }
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, EntityPlayer playerIn) {

        EntityDog dog = new EntityDog(worldIn);

        dog.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.setOwnerId(playerIn.getUniqueID());
        worldIn.spawnEntity(dog);
        dog.playAmbientSound();

        return dog;
    }
       
}
