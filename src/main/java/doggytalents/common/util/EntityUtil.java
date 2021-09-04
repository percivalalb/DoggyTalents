package doggytalents.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

public class EntityUtil {

    public static double getFollowRange(LivingEntity entityIn) {
        AttributeInstance rangeAttribute = entityIn.getAttribute(Attributes.FOLLOW_RANGE);
        return rangeAttribute == null ? 16.0D : rangeAttribute.getValue();
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigation navigator, LivingEntity target, int radius) {
        return tryToTeleportNearEntity(entityIn, navigator, target.blockPosition(), radius);
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigation navigator, BlockPos targetPos, int radius) {
        for (int i = 0; i < 10; ++i) {
            int j = getRandomNumber(entityIn, -radius, radius);
            int k = getRandomNumber(entityIn, -1, 1);
            int l = getRandomNumber(entityIn, -radius, radius);
            boolean flag = tryToTeleportToLocation(entityIn, navigator, targetPos, targetPos.getX() + j, targetPos.getY() + k, targetPos.getZ() + l);
            if (flag) {
                return true;
            }
        }

        return false;
    }

    public static boolean tryToTeleportToLocation(LivingEntity entityIn, PathNavigation navigator, BlockPos targetPos, int x, int y, int z) {
        if (Math.abs(x - targetPos.getX()) < 2.0D && Math.abs(z - targetPos.getZ()) < 2.0D) {
            return false;
        } else if (!isTeleportFriendlyBlock(entityIn, new BlockPos(x, y, z), false)) {
            return false;
        } else {
            entityIn.moveTo(x + 0.5F, y, z + 0.5F, entityIn.getYRot(), entityIn.getXRot());
            navigator.stop();
            return true;
        }
    }

    private static boolean isTeleportFriendlyBlock(LivingEntity entityIn, BlockPos pos, boolean teleportToLeaves) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(entityIn.level, pos.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = entityIn.level.getBlockState(pos.below());
            if (!teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(entityIn.blockPosition());
                return entityIn.level.noCollision(entityIn, entityIn.getBoundingBox().move(blockpos));
            }
        }
    }

    public static int getRandomNumber(LivingEntity entityIn, int minIn, int maxIn) {
        return entityIn.getRandom().nextInt(maxIn - minIn + 1) + minIn;
    }

    public static boolean isHolding(@Nullable Entity entity, Item item, Predicate<CompoundTag> nbtPredicate) {
        return isHolding(entity, stack -> stack.getItem() == item && stack.hasTag() && nbtPredicate.test(stack.getTag()));
    }

    public static boolean isHolding(@Nullable Entity entity, Item item) {
        return isHolding(entity, stack -> stack.getItem() == item);
    }

    public static boolean isHolding(@Nullable Entity entity, Predicate<ItemStack> matcher) {
        if (entity == null) {
            return false;
        }

        Iterator<ItemStack> heldItems = entity.getHandSlots().iterator();
        while (heldItems.hasNext()) {
            ItemStack stack = heldItems.next();
            if (matcher.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public static <T extends Entity> T getClosestTo(Entity center, Iterable<T> entities) {
        return getClosestTo(center.position(), entities);
    }

    public static <T extends Entity> T getClosestTo(Vec3 posVec, Iterable<T> entities) {
        double smallestDist = Double.MAX_VALUE;
        T closest = null;

        for (T entity : entities) {
            double distance = posVec.distanceToSqr(entity.position());
            if (distance < smallestDist) {
                closest = entity;
                smallestDist = distance;
            }
        }

        return closest;
    }

    public static class Sorter implements Comparator<Entity> {

        private final Vec3 vec3d;

        public Sorter(Entity entityIn) {
            this.vec3d = entityIn.position();
        }

        public Sorter(Vec3 vec3d) {
            this.vec3d = vec3d;
        }

        @Override
        public int compare(Entity entity1, Entity entity2) {
            double d0 = this.vec3d.distanceToSqr(entity1.position());
            double d1 = this.vec3d.distanceToSqr(entity2.position());

            return Double.compare(d0, d1);
        }
    }
}
