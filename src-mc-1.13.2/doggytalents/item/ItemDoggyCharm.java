package doggytalents.item;

import java.util.Objects;

import doggytalents.ModEntities;
import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
		World world = context.getWorld();
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		} else {
			EntityPlayer player = context.getPlayer();
			ItemStack itemstack = context.getItem();
			BlockPos blockpos = context.getPos();
	   		EnumFacing enumfacing = context.getFace();
	   		IBlockState iblockstate = world.getBlockState(blockpos);

	   		BlockPos blockpos1;
	   		if(iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
	   			blockpos1 = blockpos;
	   		} else {
	   			blockpos1 = blockpos.offset(enumfacing);
	   		}

	   		
	   		Entity entity = ModEntities.DOG.spawn(world, itemstack, context.getPlayer(), blockpos1, true, !Objects.equals(blockpos, blockpos1) && enumfacing == EnumFacing.UP);
	   		if(entity instanceof EntityDog) {
	   			EntityDog dog = (EntityDog)entity;
	   			if(player != null) {
		   			dog.setTamed(true);
		   			dog.setOwnerId(player.getUniqueID());
	   			}
	   			itemstack.shrink(1);
	   		}
	    	  
	   		return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(worldIn.isRemote) {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		} else {
			RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
			if(raytraceresult != null && raytraceresult.type == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();
				if(!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockFlowingFluid)) {
					return new ActionResult<>(EnumActionResult.PASS, itemstack);
				} else if(worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
					Entity entity = ModEntities.DOG.spawn(worldIn, itemstack, playerIn, blockpos, false, false);
					if(entity instanceof EntityDog) {
			   			EntityDog dog = (EntityDog)entity;
			   			dog.setTamed(true);
			   			dog.setOwnerId(playerIn.getUniqueID());
			   			itemstack.shrink(1);

						playerIn.addStat(StatList.ITEM_USED.get(this));
						return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
					} else {
						return new ActionResult<>(EnumActionResult.PASS, itemstack);
					}
				} else {
					return new ActionResult<>(EnumActionResult.FAIL, itemstack);
				}
			} else {
				return new ActionResult<>(EnumActionResult.PASS, itemstack);
			}
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
