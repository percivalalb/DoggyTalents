package doggytalents.api.inferface;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author ProPercivalalb
 */
public abstract class Talent extends ForgeRegistryEntry<Talent> {

    @Nullable
    private String translationKey, translationInfoKey;

    public void onClassCreation(IDogEntity dogIn) {}
    public void writeAdditional(IDogEntity dogIn, CompoundNBT compound) {}
    public void readAdditional(IDogEntity dogIn, CompoundNBT compound) {}

    /**
     * PASS will indicate no action is required
     * SUCCESS and FAIL results are passed to the final interact
     */
    public ActionResultType onInteract(IDogEntity dogIn, PlayerEntity playerIn, Hand handIn) {
        return ActionResultType.PASS;
    }

    public void tick(IDogEntity dog) {}
    public void livingTick(IDogEntity dog) {}
    public int onHungerTick(IDogEntity dog, int totalInTick) { return totalInTick; }
    public int onRegenerationTick(IDogEntity dog, int totalInTick) { return totalInTick; }
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) { return damage; }
    public int changeFoodValue(IDogEntity dog, ItemStack stack, int foodValue) { return foodValue; }
    public boolean isPostionApplicable(IDogEntity dog, EffectInstance potionEffect) { return true; }
    public double addToMoveSpeed(IDogEntity dog) { return 0.0D; }
    public boolean canBreatheUnderwater(IDogEntity dog) { return false; }
    public boolean canTriggerWalking(IDogEntity dog) { return true; }
    public boolean isImmuneToFalls(IDogEntity dog) { return false; }

    /**
     * Will apply the reduction in number of blocks fell when result is SUCCESS
     * PASS and FAIL will have no effect
     */
    public ActionResult<Integer> fallProtection(IDogEntity dog) {
        return ActionResult.newResult(ActionResultType.PASS, 0);
    }
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) { return true; }
    public boolean shouldDamageMob(IDogEntity dog, Entity entity) { return true; }
    public boolean canAttack(IDogEntity dog, EntityType<?> entityType) { return false; }
    public boolean canAttackEntity(IDogEntity dog, Entity entity) { return false; }
    public boolean setFire(IDogEntity dog, int amount) { return true; }
    public ActionResultType canBeRiddenInWater(IDogEntity dog, Entity rider) { return ActionResultType.PASS; }
    public void onFinishShaking(IDogEntity dogIn, boolean gotWetInWater) {}
    public boolean shouldDecreaseAir(IDogEntity dogIn, int air) { return true; }
    public void onLevelSet(IDogEntity dog, int postLevel) {}
    public void onLevelReset(IDogEntity dog, int preLevel) {}

    public <T> LazyOptional<T> getCapability(IDogEntity dogIn, Capability<T> cap, Direction side) { return null; }
    public void invalidateCapabilities(IDogEntity dogIn) {}

    public int getHighestLevel(IDogEntity dog) { return 5; }
    //public int getTotalCost(IDogEntity dog) { return 15; }

    public int getCumulativeCost(IDogEntity dog, int level) {
        switch(level) {
        case 1: return 1;
        case 2: return 3;
        case 3: return 6;
        case 4: return 10;
        case 5: return 15;
        default: return 0;
        }
    }

    public int getCost(IDogEntity dog, int level) {
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
