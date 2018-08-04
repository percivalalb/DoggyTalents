package doggytalents.talent;

import java.util.Iterator;
import java.util.List;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class RoaringGale extends ITalent {
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("roarcooldown", 0);
	}
	
	@Override
	public void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {
		int roarCooldown = (Integer)dog.objects.get("roarcooldown");
		tagCompound.setInteger("roarcooldown", roarCooldown);
	}
	
	@Override
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		dog.objects.put("roarcooldown", tagCompound.getInteger("roarcooldown"));
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		int masterOrder = dog.masterOrder();
		int roarCooldown = (Integer)dog.objects.get("roarcooldown");

		if (dog.isClient() || masterOrder != 4 || dog.getHealth() <= Constants.lowHealthLevel || dog.isChild() || level < 1) {
			return;
		}

		if (roarCooldown > 0) {
			roarCooldown--;
			dog.objects.put("roarcooldown", roarCooldown);
			return;
			// System.out.println(roarCooldown);
		}
		
		/**
		 * 
		 */
		roarCooldown = level == 5 ? 40 : 60;
		dog.objects.put("roarcooldown", roarCooldown);
		
		byte damage = (byte)(level > 4 ? level*2 : level);
		
		/**
		 * If level = 1, set duration to  20 ticks (1 second); level = 2, set duration to 24 ticks (1.2 seconds)
		 * If level = 3, set duration to 36 ticks (1.8 seconds); If level = 4, set duration to 48 ticks (2.4 seconds)
		 * If level = max (5), set duration to 70 ticks (3.5 seconds); 
		 * */
		byte effectDuration = (byte)(level > 4 ? level*14 : level*(level == 1 ? 20 : 12));
		byte knockback = (byte)level;
		
		
		List<EntityLiving> list = dog.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, dog.getEntityBoundingBox().grow(level * 4, 4D, level * 4).expand(0.0D, (double) dog.world.getHeight(), 0.0D));
		for (EntityLiving mob : list) {
			if(mob instanceof IMob) {
				dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, 1f, 1f);
				mob.attackEntityFrom(DamageSource.GENERIC, damage);
				mob.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, effectDuration, 127, false, false));
				mob.addPotionEffect(new PotionEffect(MobEffects.GLOWING, effectDuration, 1, false, false));
				mob.addVelocity(MathHelper.sin(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, -MathHelper.cos(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
			}
		}
	}

	@Override
	public String getKey() {
		return "roaringgale";
	}

}
