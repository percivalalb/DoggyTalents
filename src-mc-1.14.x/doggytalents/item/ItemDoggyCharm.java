package doggytalents.item;

import java.util.Objects;

import doggytalents.ModEntities;
import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
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
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			PlayerEntity player = context.getPlayer();
			ItemStack itemstack = context.getItem();
			BlockPos blockpos = context.getPos();
	   		Direction enumfacing = context.getFace();
	   		BlockState iblockstate = world.getBlockState(blockpos);

	   		BlockPos blockpos1;
	   		if(iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
	   			blockpos1 = blockpos;
	   		} else {
	   			blockpos1 = blockpos.offset(enumfacing);
	   		}

	   		
	   		Entity entity = ModEntities.DOG.func_220331_a(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP, false);
	   		if(entity instanceof EntityDog) {
	   			EntityDog dog = (EntityDog)entity;
	   			if(player != null) {
		   			dog.setTamed(true);
		   			dog.setOwnerId(player.getUniqueID());
	   			}
	   			itemstack.shrink(1);
	   		}
	    	  
	   		return ActionResultType.SUCCESS;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(worldIn.isRemote) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
			RayTraceResult raytraceresult = Item.func_219968_a(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
			if(raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getPos();
				if(!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
					return new ActionResult<>(ActionResultType.PASS, itemstack);
				} else if(worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, ((BlockRayTraceResult)raytraceresult).getFace(), itemstack)) {
					Entity entity = ModEntities.DOG.func_220331_a(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false);
					if(entity instanceof EntityDog) {
			   			EntityDog dog = (EntityDog)entity;
			   			dog.setTamed(true);
			   			dog.setOwnerId(playerIn.getUniqueID());
			   			itemstack.shrink(1);

						playerIn.addStat(Stats.ITEM_USED.get(this));
						return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
					} else {
						return new ActionResult<>(ActionResultType.PASS, itemstack);
					}
				} else {
					return new ActionResult<>(ActionResultType.FAIL, itemstack);
				}
			} else {
				return new ActionResult<>(ActionResultType.PASS, itemstack);
			}
		}
	}
	
    public Entity spawnCreature(World worldIn, double x, double y, double z, PlayerEntity playerIn) {

        EntityDog dog = ModEntities.DOG.create(worldIn);

        dog.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.setOwnerId(playerIn.getUniqueID());
        worldIn.func_217376_c(dog);
        dog.playAmbientSound();

        return dog;
    }
       
}
