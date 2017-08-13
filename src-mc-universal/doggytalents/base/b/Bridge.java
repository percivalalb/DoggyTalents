package doggytalents.base.b;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.base.IBridge;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
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
		return world.getEntitiesWithinAABB(classEntity, AxisAlignedBB.fromBounds(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).grow(xG, yG, zG));
	}
	
	@Override
	public void playSound(Entity entity, String name, float volume, float pitch) {
		entity.playSound(name, volume, pitch);
	}
	
	@Override
	public List<ItemStack> getHeldItems(EntityPlayer player) {
		return Lists.<ItemStack>newArrayList(player.getHeldItem());
	}
	
	@Override
	public String translateToLocal(String key) {
		return StatCollector.translateToLocal(key);
	}

	@Override
	public String translateToLocalFormatted(String key, Object... format) {
		return StatCollector.translateToLocalFormatted(key, format);
	}
	
	@Override
	public void addTranslatedMessage(EntityPlayer player, String key, Object... format) {
		player.addChatComponentMessage(new ChatComponentTranslation(key, format));
	}
	
	@Override
	public void addMessage(EntityPlayer player, String message) {
		player.addChatComponentMessage(new ChatComponentText(message));
	}
	
	@Override
	public void addJumpFromPotion(EntityDog dog) {
		if(dog.isPotionActive(Potion.jump))
			dog.motionY += (double)((float)(dog.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
	}
	
	@Override
	public Entity getRidingEntity(EntityPlayer player) {
		return player.ridingEntity;
	}
	
	@Override
	public boolean isPosion(PotionEffect potionEffect) {
		return potionEffect.getPotionID() == Potion.poison.getId();
	}
	
	@Override
	public void addPosion(EntityLivingBase entity, int effectDuration, int effectAmplifier) {
		entity.addPotionEffect(new PotionEffect(Potion.poison.getId(), effectDuration, effectAmplifier));
	}
	
	@Override
	public void addNightVision(EntityLivingBase entity, int effectDuration, int effectAmplifier) {
		entity.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), effectDuration, effectAmplifier, true, false));
	}

	@Override
	public int clamp(int num, int min, int max) {
		return MathHelper.clamp_int(num, min, max);
	}
	
	@Override
	public float clamp(float num, float min, float max) {
		return MathHelper.clamp_float(num, min, max);
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
		return MathHelper.floor_double(value);
	}
	
	@Override
	public int ceil(double value) {
		return MathHelper.ceiling_double_int(value);
	}
	
	@Override
	public float sqrt(double value) {
		return MathHelper.sqrt_double(value);
	}

	@Override
	public double atan2(double x, double z) {
		return MathHelper.atan2(x, z);
	}
	
	@Override
	public float wrapDegrees(float value) {
		return MathHelper.wrapAngleTo180_float(value);
	}
}
