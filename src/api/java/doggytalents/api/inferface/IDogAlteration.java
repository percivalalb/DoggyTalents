package doggytalents.api.inferface;

import doggytalents.api.enu.WetSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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

    default void onWrite(AbstractDogEntity dogIn, CompoundTag compound) {

    }

    default void onRead(AbstractDogEntity dogIn, CompoundTag compound) {

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

    default InteractionResultHolder<Integer> hungerTick(AbstractDogEntity dogIn, int hungerTick) {
        return InteractionResultHolder.pass(hungerTick);
    }

    default InteractionResultHolder<Integer> healingTick(AbstractDogEntity dogIn, int healingTick) {
        return InteractionResultHolder.pass(healingTick);
    }

    default InteractionResult processInteract(AbstractDogEntity dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        return InteractionResult.PASS;
    }

    default InteractionResult canBeRiddenInWater(AbstractDogEntity dogIn, Entity rider) {
        return InteractionResult.PASS;
    }

    default InteractionResult canTrample(AbstractDogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        return InteractionResult.PASS;
    }

    default InteractionResultHolder<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        return InteractionResultHolder.pass(0F);
    }

    default InteractionResult canBreatheUnderwater(AbstractDogEntity dogIn) {
        return InteractionResult.PASS;
    }

    default InteractionResult canAttack(AbstractDogEntity dogIn, LivingEntity target) {
        return InteractionResult.PASS;
    }

    default InteractionResult canAttack(AbstractDogEntity dogIn, EntityType<?> entityType) {
        return InteractionResult.PASS;
    }

    default InteractionResult shouldAttackEntity(AbstractDogEntity dog, LivingEntity target, LivingEntity owner) {
        return InteractionResult.PASS;
    }

    default InteractionResult hitByEntity(AbstractDogEntity dog, Entity entity) {
        return InteractionResult.PASS;
    }

    default InteractionResult attackEntityAsMob(AbstractDogEntity dogIn, Entity target) {
        return InteractionResult.PASS;
    }


    default InteractionResultHolder<Float> attackEntityFrom(AbstractDogEntity dog, DamageSource source, float damage) {
        return InteractionResultHolder.pass(damage);
    }

    default InteractionResult canBlockDamageSource(AbstractDogEntity dog, DamageSource source) {
        return InteractionResult.PASS;
    }

    default void onDeath(AbstractDogEntity dog, DamageSource source) {

    }

    default void spawnDrops(AbstractDogEntity dog, DamageSource source) {

    }

    default void dropLoot(AbstractDogEntity dog, DamageSource source, boolean recentlyHitIn) {

    }

    default void dropInventory(AbstractDogEntity dogIn) {

    }

    default InteractionResultHolder<Float> attackEntityFrom(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        return InteractionResultHolder.pass(distance);
    }

    default InteractionResultHolder<Integer> decreaseAirSupply(AbstractDogEntity dogIn, int air) {
        return InteractionResultHolder.pass(air);
    }

    default InteractionResultHolder<Integer> determineNextAir(AbstractDogEntity dogIn, int currentAir) {
        return InteractionResultHolder.pass(currentAir);
    }

    default InteractionResultHolder<Integer> setFire(AbstractDogEntity dogIn, int second) {
        return InteractionResultHolder.pass(second);
    }

    default InteractionResult isImmuneToFire(AbstractDogEntity dogIn) {
        return InteractionResult.PASS;
    }

    default InteractionResult isInvulnerableTo(AbstractDogEntity dogIn, DamageSource source) {
        return InteractionResult.PASS;
    }

    default InteractionResult isInvulnerable(AbstractDogEntity dogIn) {
        return InteractionResult.PASS;
    }

    default InteractionResult onLivingFall(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        return InteractionResult.PASS;
    }

    default <T> LazyOptional<T> getCapability(AbstractDogEntity dogIn, Capability<T> cap, Direction side) {
        return null;
    }

    default void invalidateCapabilities(AbstractDogEntity dogIn) {

    }

    default InteractionResultHolder<Float> getMaxHunger(AbstractDogEntity dogIn, float currentMax) {
        return InteractionResultHolder.pass(currentMax);
    }

    default InteractionResultHolder<Float> setDogHunger(AbstractDogEntity dogIn, float hunger, float diff) {
        return InteractionResultHolder.pass(hunger);
    }

    default InteractionResult isPotionApplicable(AbstractDogEntity dogIn, MobEffectInstance effectIn) {
        return InteractionResult.PASS;
    }

    /**
     * Only called serverside
     * @param dogIn The dog
     * @param source How the dog initially got wet
     */
    default void onShakingDry(AbstractDogEntity dogIn, WetSource source) {

    }
}
