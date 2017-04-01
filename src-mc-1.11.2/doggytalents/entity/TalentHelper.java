package doggytalents.entity;

import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

/**
 * @author ProPercivalalb
 */
public class TalentHelper {

	public static void onClassCreation(EntityDog dog) {
		for(ITalent talent : TalentRegistry.getTalents())
			talent.onClassCreation(dog);
	}
	
	public static void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {
		for(ITalent talent : TalentRegistry.getTalents())
			talent.writeToNBT(dog, tagCompound);
	}
	
	public static void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		for(ITalent talent : TalentRegistry.getTalents())
			talent.readFromNBT(dog, tagCompound);
	}
	
	public static boolean interactWithPlayer(EntityDog dog, EntityPlayer player) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(talent.interactWithPlayer(dog, player))
				return true;
		return false;
	}

	public static void onUpdate(EntityDog  dog) {
		for(ITalent talent : TalentRegistry.getTalents())
			talent.onUpdate(dog);
	}
	
	public static void onLivingUpdate(EntityDog dog) {
		for(ITalent talent : TalentRegistry.getTalents())
			talent.onLivingUpdate(dog);
	}
	
	public static int onHungerTick(EntityDog dog, int totalInTick) {
		int total = totalInTick;
		for(ITalent talent : TalentRegistry.getTalents())
			total = talent.onHungerTick(dog, total);
		return total;
	}
	
	public static int onRegenerationTick(EntityDog dog, int totalInTick) {
		int total = totalInTick;
		for(ITalent talent : TalentRegistry.getTalents())
			total = talent.onRegenerationTick(dog, total);
		return total;
	}

	public static int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
		int total = damage;
		for(ITalent talent : TalentRegistry.getTalents())
			total = talent.attackEntityAsMob(dog, entity, total);
		return total;
	}
	
	public static int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) {
		int total = foodValue;
		for(ITalent talent : TalentRegistry.getTalents())
			total = talent.changeFoodValue(dog, stack, total);
		return total;
	}
	
	public static int getUsedPoints(EntityDog dog) {
		int total = 0;
		for(ITalent talent : TalentRegistry.getTalents())
			total += talent.getCumulativeCost(dog, dog.talents.getLevel(talent));
		return total;
	}

	public static boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(!talent.isPostionApplicable(dog, potionEffect))
				return false;
		return true;
	}

	public static double addToMoveSpeed(EntityDog dog) {
		double total = 0;
		for(ITalent talent : TalentRegistry.getTalents())
			total += talent.addToMoveSpeed(dog);
		return total;
	}

	public static boolean canBreatheUnderwater(EntityDog dog) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(talent.canBreatheUnderwater(dog))
				return true;
		return false;
	}

	public static boolean isImmuneToFalls(EntityDog dog) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(talent.isImmuneToFalls(dog))
				return true;
		return false;
	}
	
	public static int fallProtection(EntityDog dog) {
		int total = 0;
		for(ITalent talent : TalentRegistry.getTalents())
			total += talent.fallProtection(dog);
		return total;
	}

	public static boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(!talent.attackEntityFrom(dog, damageSource, damage))
				return false;
		return true;
	}

	public static boolean shouldDamageMob(EntityDog dog, Entity entity) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(!talent.shouldDamageMob(dog, entity))
				return false;
		return true;
	}

	public static SoundEvent getLivingSound(EntityDog dog) {
		for(ITalent talent : TalentRegistry.getTalents()) {
			SoundEvent sound = talent.getLivingSound(dog);
			if(sound != null)
				return sound;
		}
		return null;
	}

	public static boolean canAttackClass(EntityDog dog, Class entityClass) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(talent.canAttackClass(dog, entityClass))
				return true;
		return false;
	}
	
	public static boolean canAttackEntity(EntityDog dog, Entity entity) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(talent.canAttackEntity(dog, entity))
				return true;
		return false;
	}

	public static boolean setFire(EntityDog dog, int amount) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(!talent.setFire(dog, amount))
				return false;
		return true;
	}
	
	public static boolean shouldDismountInWater(EntityDog dog, Entity rider) {
		for(ITalent talent : TalentRegistry.getTalents())
			if(!talent.shouldDismountInWater(dog, rider))
				return false;
		return true;
	}
}
