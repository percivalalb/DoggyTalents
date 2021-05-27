package doggytalents.api.inferface;

import doggytalents.api.enu.WetSource;
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

    /**
     * Called when ever this instance is first added to a dog, this is called when
     * the level is first set on the dog or when it is loaded from NBT and when the
     * talents are synced to the client
     *
     * @param dogIn The dog
     */
    default void init(AbstractDogEntity dogIn) {

    }

    default void remove(AbstractDogEntity dogIn) {

    }

    default void onWrite(AbstractDogEntity dogIn, CompoundNBT compound) {

    }

    default void onRead(AbstractDogEntity dogIn, CompoundNBT compound) {

    }

    /**
     * Called at the end of tick
     */
    default void tick(AbstractDogEntity dogIn) {

    }

    /**
     * Called at the end of livingTick
     */
    default void livingTick(AbstractDogEntity dogIn) {

    }

    default ActionResult<Integer> hungerTick(AbstractDogEntity dogIn, int hungerTick) {
        return ActionResult.resultPass(hungerTick);
    }

    default ActionResult<Integer> healingTick(AbstractDogEntity dogIn, int healingTick) {
        return ActionResult.resultPass(healingTick);
    }

    default ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType canBeRiddenInWater(AbstractDogEntity dogIn, Entity rider) {
        return ActionResultType.PASS;
    }

    default ActionResultType canTrample(AbstractDogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        return ActionResultType.PASS;
    }

    default ActionResult<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        return ActionResult.resultPass(0F);
    }

    default ActionResultType canBreatheUnderwater(AbstractDogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType canAttack(AbstractDogEntity dogIn, LivingEntity target) {
        return ActionResultType.PASS;
    }

    default ActionResultType canAttack(AbstractDogEntity dogIn, EntityType<?> entityType) {
        return ActionResultType.PASS;
    }

    default ActionResultType shouldAttackEntity(AbstractDogEntity dog, LivingEntity target, LivingEntity owner) {
        return ActionResultType.PASS;
    }

    default ActionResultType hitByEntity(AbstractDogEntity dog, Entity entity) {
        return ActionResultType.PASS;
    }

    default ActionResultType attackEntityAsMob(AbstractDogEntity dogIn, Entity target) {
        return ActionResultType.PASS;
    }


    default ActionResult<Float> attackEntityFrom(AbstractDogEntity dog, DamageSource source, float damage) {
        return ActionResult.resultPass(damage);
    }

    default ActionResultType canBlockDamageSource(AbstractDogEntity dog, DamageSource source) {
        return ActionResultType.PASS;
    }

    default void onDeath(AbstractDogEntity dog, DamageSource source) {

    }

    default void spawnDrops(AbstractDogEntity dog, DamageSource source) {

    }

    default void dropLoot(AbstractDogEntity dog, DamageSource source, boolean recentlyHitIn) {

    }

    default void dropInventory(AbstractDogEntity dogIn) {

    }

    default ActionResult<Float> attackEntityFrom(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        return ActionResult.resultPass(distance);
    }

    default ActionResult<Integer> decreaseAirSupply(AbstractDogEntity dogIn, int air) {
        return ActionResult.resultPass(air);
    }

    default ActionResult<Integer> determineNextAir(AbstractDogEntity dogIn, int currentAir) {
        return ActionResult.resultPass(currentAir);
    }

    default ActionResult<Integer> setFire(AbstractDogEntity dogIn, int second) {
        return ActionResult.resultPass(second);
    }

    default ActionResultType isImmuneToFire(AbstractDogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType isInvulnerableTo(AbstractDogEntity dogIn, DamageSource source) {
        return ActionResultType.PASS;
    }

    default ActionResultType isInvulnerable(AbstractDogEntity dogIn) {
        return ActionResultType.PASS;
    }

    default ActionResultType onLivingFall(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        return ActionResultType.PASS;
    }

    default <T> LazyOptional<T> getCapability(AbstractDogEntity dogIn, Capability<T> cap, Direction side) {
        return null;
    }

    default void invalidateCapabilities(AbstractDogEntity dogIn) {

    }

    default ActionResult<Float> getMaxHunger(AbstractDogEntity dogIn, float currentMax) {
        return ActionResult.resultPass(currentMax);
    }

    default ActionResult<Float> setDogHunger(AbstractDogEntity dogIn, float hunger, float diff) {
        return ActionResult.resultPass(hunger);
    }

    default ActionResultType isPotionApplicable(AbstractDogEntity dogIn, EffectInstance effectIn) {
        return ActionResultType.PASS;
    }

    /**
     * Only called serverside
     * @param dogIn The dog
     * @param source How the dog initially got wet
     */
    default void onShakingDry(AbstractDogEntity dogIn, WetSource source) {

    }
}
