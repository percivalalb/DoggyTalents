package doggytalents.api.inferface;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;

/**
 * @author ProPercivalalb
 */
public abstract class ITalent {
	
	public void onClassCreation(EntityDog dog) {}
	public void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {}
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {}
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player) { return false; }
	public void onUpdate(EntityDog dog) {}
	public void onLivingUpdate(EntityDog dog) {}
	public int onHungerTick(EntityDog dog, int totalInTick) { return totalInTick; }
	public int onRegenerationTick(EntityDog dog, int totalInTick) { return totalInTick; }
	public int attackEntityAsMob(EntityDog dog, Entity entity, int damage) { return damage; }
	public int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) { return foodValue; }
	public boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) { return true; }
	public double addToMoveSpeed(EntityDog dog) { return 0.0D; }
	public boolean canBreatheUnderwater(EntityDog dog) { return false; }
	public boolean canTriggerWalking(EntityDog dog) { return true; }
	public boolean isImmuneToFalls(EntityDog dog) { return false; }
	public int fallProtection(EntityDog dog) { return 0; }
	public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) { return true; }
	public boolean shouldDamageMob(EntityDog dog, Entity entity) { return true; }
	public SoundEvent getLivingSound(EntityDog dog) { return null; }
	public boolean canAttackClass(EntityDog dog, Class entityClass) { return false; }
	public boolean canAttackEntity(EntityDog dog, Entity entity) { return false; }
	public boolean setFire(EntityDog dog, int amount) { return true; }
	public boolean shouldDismountInWater(EntityDog dog, Entity rider) { return true; }
	
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
	
	public String getLocalisedName() {
		return I18n.translateToLocal("doggui.talentname." + this.getKey());
	}
	
	public String getLocalisedInfo() {
		return I18n.translateToLocal("doggui.talentinfo." + this.getKey());
	}
	
	/**
	 * If you can try and keep the key as short as possible because
	 * it has to be sent to the client and server constantly so to 
	 * avoid packet size kept as small as possible
	 * @return The key that can be used to look up this talent
	 */
	public abstract String getKey();
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ITalent)
			return ((ITalent)obj).getKey().equals(this.getKey());
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getKey().hashCode();
	}
}
