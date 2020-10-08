package doggytalents.common.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class EntityUtil {

    public static double getFollowRange(LivingEntity entityIn) {
        ModifiableAttributeInstance rangeAttribute = entityIn.getAttribute(Attributes.FOLLOW_RANGE);
        return rangeAttribute == null ? 16.0D : rangeAttribute.getValue();
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigator navigator, LivingEntity target, int radius) {
        return tryToTeleportNearEntity(entityIn, navigator, target.getPosition(), radius);
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigator navigator, BlockPos targetPos, int radius) {
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

    public static boolean tryToTeleportToLocation(LivingEntity entityIn, PathNavigator navigator, BlockPos targetPos, int x, int y, int z) {
        if (Math.abs(x - targetPos.getX()) < 2.0D && Math.abs(z - targetPos.getZ()) < 2.0D) {
            return false;
        } else if (!isTeleportFriendlyBlock(entityIn, new BlockPos(x, y, z), false)) {
            return false;
        } else {
            entityIn.setLocationAndAngles(x + 0.5F, y, z + 0.5F, entityIn.rotationYaw, entityIn.rotationPitch);
            navigator.clearPath();
            return true;
        }
    }

    private static boolean isTeleportFriendlyBlock(LivingEntity entityIn, BlockPos pos, boolean teleportToLeaves) {
        PathNodeType pathnodetype = WalkNodeProcessor.func_237231_a_(entityIn.world, pos.toMutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = entityIn.world.getBlockState(pos.down());
            if (!teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(entityIn.getPosition());
                return entityIn.world.hasNoCollisions(entityIn, entityIn.getBoundingBox().offset(blockpos));
            }
        }
    }

    public static int getRandomNumber(LivingEntity entityIn, int minIn, int maxIn) {
        return entityIn.getRNG().nextInt(maxIn - minIn + 1) + minIn;
    }

    public static boolean isHolding(@Nullable Entity entity, Item item, Predicate<CompoundNBT> nbtPredicate) {
        return isHolding(entity, stack -> stack.getItem() == item && stack.hasTag() && nbtPredicate.test(stack.getTag()));
    }

    public static boolean isHolding(@Nullable Entity entity, Item item) {
        return isHolding(entity, stack -> stack.getItem() == item);
    }

    public static boolean isHolding(@Nullable Entity entity, Predicate<ItemStack> matcher) {
        if (entity == null) {
            return false;
        }

        Iterator<ItemStack> heldItems = entity.getHeldEquipment().iterator();
        while (heldItems.hasNext()) {
            ItemStack stack = heldItems.next();
            if (matcher.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public static class Sorter implements Comparator<Entity> {

        private final Vector3d vec3d;

        public Sorter(Entity entityIn) {
            this.vec3d = entityIn.getPositionVec();
        }

        public Sorter(Vector3d vec3d) {
            this.vec3d = vec3d;
        }

        @Override
        public int compare(Entity entity1, Entity entity2) {
            double d0 = this.vec3d.squareDistanceTo(entity1.getPositionVec());
            double d1 = this.vec3d.squareDistanceTo(entity2.getPositionVec());

            return Double.compare(d0, d1);
        }
    }
}
