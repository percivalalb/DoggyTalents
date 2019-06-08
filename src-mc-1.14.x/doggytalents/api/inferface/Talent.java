package doggytalents.api.inferface;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author ProPercivalalb
 */
public abstract class Talent extends ForgeRegistryEntry<Talent> {
	
	@Nullable
	private String translationKey, translationInfoKey;
	
	public void onClassCreation(EntityDog dogIn) {}
	public void writeAdditional(EntityDog dogIn, NBTTagCompound compound) {}
	public void readAdditional(EntityDog dogIn, NBTTagCompound compound) {}
	
	/**
	 * PASS will indicate no action is required
	 * SUCCESS and FAIL results are passed to the final interact
	 */
	public ActionResult<ItemStack> onInteract(EntityDog dogIn, EntityPlayer playerIn, ItemStack stackIn) { 
		return ActionResult.newResult(EnumActionResult.PASS, stackIn); 
	}
	
	public void tick(EntityDog dog) {}
	public void livingTick(EntityDog dog) {}
	public int onHungerTick(EntityDog dog, int totalInTick) { return totalInTick; }
	public int onRegenerationTick(EntityDog dog, int totalInTick) { return totalInTick; }
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) { return damage; }
	public int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) { return foodValue; }
	public boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) { return true; }
	public double addToMoveSpeed(EntityDog dog) { return 0.0D; }
	public boolean canBreatheUnderwater(EntityDog dog) { return false; }
	public boolean canTriggerWalking(EntityDog dog) { return true; }
	public boolean isImmuneToFalls(EntityDog dog) { return false; }
	
	/**
	 * Will apply the reduction in number of blocks fell when result is SUCCESS
	 * PASS and FAIL will have no effect
	 */
	public ActionResult<Integer> fallProtection(EntityDog dog) { 
		return ActionResult.newResult(EnumActionResult.PASS, 0); 
	}
	public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) { return true; }
	public boolean shouldDamageMob(EntityDog dog, Entity entity) { return true; }
	public boolean canAttackClass(EntityDog dog, Class entityClass) { return false; }
	public boolean canAttackEntity(EntityDog dog, Entity entity) { return false; }
	public boolean setFire(EntityDog dog, int amount) { return true; }
	public boolean shouldDismountInWater(EntityDog dog, Entity rider) { return true; }
	public void onLevelSet(EntityDog dog, int level) {}
	
	
	public int getHighestLevel(EntityDog dog) { return 5; }
	//public int getTotalCost(EntityDog dog) { return 15; }
	
	public int getCumulativeCost(EntityDog dog, int level) {
		switch(level) {
        case 1: return 1;
		case 2: return 3;
        case 3: return 6;
        case 4: return 10;
        case 5: return 15;
        default: return 0;
        }
	}
	
	public int getCost(EntityDog dog, int level) {
		return level;
	}
	
	public String getTranslationKey() {
		if(this.translationKey == null) {
			this.translationKey = Util.makeTranslationKey("talent", DoggyTalentsAPI.TALENTS.getKey(this));
		}
		return this.translationKey;
	}
	
	public String getInfoTranslationKey() {
		if(this.translationInfoKey == null) {
			this.translationInfoKey = this.getTranslationKey() + ".description";
		}
		return this.translationInfoKey;
	}
}
