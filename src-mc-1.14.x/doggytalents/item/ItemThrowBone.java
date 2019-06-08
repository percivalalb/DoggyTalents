package doggytalents.item;

import doggytalents.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemThrowBone extends Item {
	
	public enum Type {
		DRY,
		WET
	}
	
	public Type type;
	
	public ItemThrowBone(Properties properties) {
		this(Type.DRY, properties);
	}
	
	public ItemThrowBone(Type type, Properties properties) {
		super(properties);
		this.type = type;
	}
	
	public void setHeadingFromThrower(ItemEntity entityItem, Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
		float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		this.setThrowableHeading(entityItem, (double)f, (double)f1, (double)f2, velocity, inaccuracy);
		Vec3d vec3d = entityThrower.getMotion();
		entityItem.setMotion(entityItem.getMotion().add(vec3d.x, entityThrower.onGround ? 0.0D : vec3d.y, vec3d.z));
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		
		if(this.type == Type.WET) {
			if(itemStackIn.getItem() == ModItems.THROW_BONE_WET)
				itemStackIn = new ItemStack(ModItems.THROW_BONE);
			else if(itemStackIn.getItem() == ModItems.THROW_STICK_WET)
				itemStackIn = new ItemStack(ModItems.THROW_STICK);

    		playerIn.swingArm(handIn);
    		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    	}
		else {
	
	        worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	
	        if(!worldIn.isRemote) {
	        	ItemStack stack = itemStackIn.copy();
	        	stack.setCount(1);
	        	ItemEntity entityitem = new ItemEntity(playerIn.world, playerIn.posX, (playerIn.posY - 0.30000001192092896D) + (double)playerIn.getEyeHeight(), playerIn.posZ, stack);
	            entityitem.setPickupDelay(40);
	            this.setHeadingFromThrower(entityitem, playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.2F, 1.0F);
                worldIn.func_217376_c(entityitem);
	        }
	        
	        if(!playerIn.playerAbilities.isCreativeMode)
	        	itemStackIn.shrink(1);

	        playerIn.addStat(Stats.ITEM_USED.get(this));
	        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
		}
	}

    public void setThrowableHeading(ItemEntity entityItem, double x, double y, double z, float velocity, float inaccuracy) {
    	Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(random.nextGaussian() * (double)0.0075F * (double)inaccuracy, random.nextGaussian() * (double)0.0075F * (double)inaccuracy, random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
    	entityItem.setMotion(vec3d);
        float f = MathHelper.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
        entityItem.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        entityItem.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
        entityItem.prevRotationYaw =  entityItem.rotationYaw;
        entityItem.prevRotationPitch = entityItem.rotationPitch;
    }
}
