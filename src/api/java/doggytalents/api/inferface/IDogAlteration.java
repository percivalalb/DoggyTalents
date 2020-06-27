package doggytalents.api.inferface;

import doggytalents.api.enu.WetSource;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.NBTUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IDogAlteration {

    default void write(DogEntity dogIn, CompoundNBT compound) {

    }

    default void read(DogEntity dogIn, CompoundNBT compound) {

    }

    /**
     * Called at the end of tick
     */
    default void tick(DogEntity dogIn) {

    }

    /**
     * Called at the end of livingTick
     */
    default void livingTick(DogEntity dogIn) {

    }

    default ActionResult<Integer> hungerTick(DogEntity dogIn, int hungerTick) {
        return ActionResult.resultPass(hungerTick);
    }

    default ActionResult<Integer> healingTick(DogEntity dogIn, int healingTick) {
        return ActionResult.resultPass(healingTick);
    }

    default ActionResultType processInteract(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType canBeRiddenInWater(DogEntity dogIn, Entity rider) {
        return ActionResultType.PASS;
    }

    default ActionResultType canTrample(DogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        return ActionResultType.PASS;
    }

    default ActionResultType canBreatheUnderwater(DogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType canAttack(DogEntity dogIn, LivingEntity target) {
        return ActionResultType.PASS;
    }

    default ActionResultType canAttack(DogEntity dogIn, EntityType<?> entityType) {
        return ActionResultType.PASS;
    }

    default ActionResultType shouldAttackEntity(DogEntity dog, LivingEntity target, LivingEntity owner) {
        return ActionResultType.PASS;
    }

    default ActionResultType hitByEntity(DogEntity dog, Entity entity) {
        return ActionResultType.PASS;
    }

    default ActionResultType attackEntityAsMob(DogEntity dogIn, Entity target) {
        return ActionResultType.PASS;
    }


    default ActionResult<Float> attackEntityFrom(DogEntity dog, DamageSource source, float damage) {
        return ActionResult.resultPass(damage);
    }

    default ActionResultType canBlockDamageSource(DogEntity dog, DamageSource source) {
        return ActionResultType.PASS;
    }

    default void onDeath(DogEntity dog, DamageSource source) {

    }

    default void spawnDrops(DogEntity dog, DamageSource source) {

    }

    default void dropLoot(DogEntity dog, DamageSource source, boolean recentlyHitIn) {

    }

    default void dropInventory(DogEntity dogIn) {

    }

    default ActionResult<Float> attackEntityFrom(DogEntity dogIn, float distance, float damageMultiplier) {
        return ActionResult.resultPass(distance);
    }

    default ActionResult<Integer> decreaseAirSupply(DogEntity dogIn, int air) {
        return ActionResult.resultPass(air);
    }

    default ActionResult<Integer> determineNextAir(DogEntity dogIn, int currentAir) {
        return ActionResult.resultPass(currentAir);
    }

    default ActionResult<Integer> setFire(DogEntity dogIn, int second) {
        return ActionResult.resultPass(second);
    }

    default ActionResultType isImmuneToFire(DogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType isInvulnerableTo(DogEntity dogIn, DamageSource source) {
        return ActionResultType.PASS;
    }

    default ActionResultType isInvulnerable(DogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default <T> LazyOptional<T> getCapability(DogEntity dogIn, Capability<T> cap, Direction side) {
        return null;
    }

    default void invalidateCapabilities(DogEntity dogIn) {

    }

    default ActionResult<Float> getMaxHunger(DogEntity dogIn, float currentMax) {
        return ActionResult.resultPass(currentMax);
    }

    default ActionResult<Float> setDogHunger(DogEntity dogIn, float hunger, float diff) {
        return ActionResult.resultPass(hunger);
    }

    default ActionResultType isPotionApplicable(DogEntity dogIn, EffectInstance effectIn) {
        return ActionResultType.PASS;
    }

    /**
     * Only called serverside
     * @param dogIn The dog
     * @param source How the dog initially got wet
     */
    default void onShakingDry(DogEntity dogIn, WetSource source) {

    }
}
