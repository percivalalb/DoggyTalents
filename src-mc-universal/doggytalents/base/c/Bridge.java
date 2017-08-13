package doggytalents.base.c;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.base.IBridge;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class Bridge implements IBridge {

	@Override
	public Object createBlockPos(int x, int y, int z) {
		return null;
	}
	
	@Override
	public TileEntity getTileEntity(World world, int x, int y, int z) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
	@Override
	public boolean isBlockLoaded(World world, int x, int y, int z) {
		return world.isBlockLoaded(new BlockPos(x, y, z));
	}
	
	@Override
	public Block getBlock(World world, int x, int y, int z) {
		return world.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
	
	@Override
	public <T extends Entity> List<T> getEntitiesWithinAABB(World world, Class<? extends T> classEntity, double x, double y, double z, int xG, int yG, int zG) {
		return world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).grow(xG, yG, zG));
	}
	
	@Override
	public void playSound(Entity entity, String name, float volume, float pitch) {
		if(name.equals("mob.wolf.shake"))
			entity.playSound(SoundEvents.ENTITY_WOLF_SHAKE, volume, pitch);
		else if(name.equals("random.chestopen"))
			entity.playSound(SoundEvents.BLOCK_CHEST_OPEN, volume, pitch);
		else if(name.equals("random.pop")) 
			entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, volume, pitch);
		else if(name.equals("random.break"))
			entity.playSound(SoundEvents.ENTITY_ITEM_BREAK, volume, pitch);
		else if(name.equals("mob.wolf.panting"))
			entity.playSound(SoundEvents.ENTITY_WOLF_PANT, volume, pitch);
		else if(name.equals("random.bow"))
			entity.playSound(SoundEvents.ENTITY_ARROW_SHOOT, volume, pitch);
	}
	
	@Override
	public List<ItemStack> getHeldItems(EntityPlayer player) {
		return Lists.<ItemStack>newArrayList(player.getHeldItem(EnumHand.MAIN_HAND), player.getHeldItem(EnumHand.OFF_HAND));
	}

	@Override
	public String translateToLocal(String key) {
		return I18n.translateToLocal(key);
	}

	@Override
	public String translateToLocalFormatted(String key, Object... format) {
		return I18n.translateToLocalFormatted(key, format);
	}
	
	@Override
	public void addTranslatedMessage(EntityPlayer player, String key, Object... format) {
		player.sendMessage(new TextComponentTranslation(key, format));
	}
	
	@Override
	public void addMessage(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(message));
	}
	
	@Override
	public void addJumpFromPotion(EntityDog dog) {
		if(dog.isPotionActive(MobEffects.JUMP_BOOST))
			dog.motionY += (double)((float)(dog.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
	}
	
	@Override
	public Entity getRidingEntity(EntityPlayer player) {
		return player.getRidingEntity();
	}
	
	@Override
	public boolean isPosion(PotionEffect potionEffect) {
		return potionEffect.getPotion() == MobEffects.POISON;
	}
	
	@Override
	public void addPosion(EntityLivingBase entity, int effectDuration, int effectAmplifier) {
		entity.addPotionEffect(new PotionEffect(MobEffects.POISON, effectDuration, effectAmplifier));
	}
	
	@Override
	public void addNightVision(EntityLivingBase entity, int effectDuration, int effectAmplifier) {
		entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, effectDuration, effectAmplifier, true, false));
	}
	
	@Override
	public int clamp(int num, int min, int max) {
		return MathHelper.clamp(num, min, max);
	}
	
	@Override
	public float clamp(float num, float min, float max) {
		return MathHelper.clamp(num, min, max);
	}
	
	@Override
	public float cos(float value) {
		return MathHelper.cos(value);
	}
	
	@Override
	public float sin(float value) {
		return MathHelper.sin(value);
	}
	
	@Override
	public int floor(double value) {
		return MathHelper.floor(value);
	}
	
	@Override
	public int ceil(double value) {
		return MathHelper.ceil(value);
	}
	
	@Override
	public float sqrt(double value) {
		return MathHelper.sqrt(value);
	}

	@Override
	public double atan2(double x, double z) {
		return MathHelper.atan2(x, z);
	}
	
	@Override
	public float wrapDegrees(float value) {
		return MathHelper.wrapDegrees(value);
	}
}
