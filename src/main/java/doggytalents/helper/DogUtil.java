package doggytalents.helper;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class DogUtil {

    public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigator pathfinder, int radius) {
        teleportDogToPos(owner.posX, owner.getBoundingBox().minY, owner.posZ, entity, world, pathfinder, radius);
    }
    
    public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigator pathfinder) {
        teleportDogToPos(owner.posX, owner.getBoundingBox().minY, owner.posZ, entity, world, pathfinder, 2);
    }
    
    public static void teleportDogToPos(double x, double y, double z, Entity entity, World world, PathNavigator pathfinder, int radius) {
        int i = MathHelper.floor(x) - radius;
        int j = MathHelper.floor(z) - radius;
        int k = MathHelper.floor(y);

        for(int l = 0; l <= radius * 2; ++l) {
            for(int i1 = 0; i1 <= radius * 2; ++i1) {
                if((l < 1 || i1 < 1 || l > radius * 2 - 1 || i1 > radius * 2 - 1) && isTeleportFriendlyBlock(entity, world, i, j, k, l, i1)) {
                    entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), entity.rotationYaw, entity.rotationPitch);
                    pathfinder.clearPath();
                    return;
                }
            }
        }
    }
    
    public static boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
        BlockPos blockpos = new BlockPos(xBase + xAdd, y - 1, zBase + zAdd);
        BlockState iblockstate = world.getBlockState(blockpos);
        return iblockstate.canEntitySpawn(world, blockpos, entity.getType()) && world.isAirBlock(blockpos.up()) && world.isAirBlock(blockpos.up(2));
    }
    
    /**
     * Tries to feed a max of one item to the dog
     */
    public static boolean feedDogFrom(EntityDog dogIn, IItemHandler source) {
        for(int i = 0; i < source.getSlots(); i++) {
            ItemStack stack = source.extractItem(i, 1, true);
            int foodValue = 0;
            if((foodValue = dogIn.foodValue(stack)) > 0) {
                stack = source.extractItem(i, 1, false);
                dogIn.setDogHunger(dogIn.getDogHunger() + foodValue);
                
                if(stack.getItem() == ModItems.CHEW_STICK) { //TODO add player paramater
                    ((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(dogIn);
                }

                return true;
            }
        }
        
        return false;
    }

    public static boolean isHolding(Entity entity, Item item, Predicate<CompoundNBT> nbtPredicate) {
        return isHolding(entity, stack -> stack.getItem() == item && stack.hasTag() && nbtPredicate.test(stack.getTag()));
    }
    
    public static boolean isHolding(Entity entity, Item item) {
        return isHolding(entity, stack -> stack.getItem() == item);
    }
    
    public static boolean isHolding(Entity entity, Predicate<ItemStack> matcher) {
        if(entity == null) {
            return false;
        }
        
        Iterator<ItemStack> heldItems = entity.getHeldEquipment().iterator();
        while(heldItems.hasNext()) {
            ItemStack stack = heldItems.next();
            if(matcher.test(stack))
                return true;
        }
        
        return false;
    }
    
    public static float[] rgbIntToFloatArray(int rgbInt) {
        int r = (rgbInt >> 16) & 255;
        int g = (rgbInt >> 8) & 255;
        int b = (rgbInt >> 0) & 255;

        return new float[] {(float)r / 255F, (float)g / 255F, (float)b / 255F};
    }
    
    public static int[] rgbIntToIntArray(int rgbInt) {
        int r = (rgbInt >> 16) & 255;
        int g = (rgbInt >> 8) & 255;
        int b = (rgbInt >> 0) & 255;

        return new int[] {r, g, b};
    }
    
    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        public Sorter(Entity entityIn) {
            this.entity = entityIn;
        }

        @Override
        public int compare(Entity entity1, Entity entity2) {
            double d0 = this.entity.getDistanceSq(entity1);
            double d1 = this.entity.getDistanceSq(entity2);
            if(d0 < d1) {
                return -1;
            } else {
                return d0 > d1 ? 1 : 0;
            }
        }
    }

    public static void transferStacks(IItemHandlerModifiable source, IItemHandler target) {
        for (int i = 0; i < source.getSlots(); i++) {
            source.setStackInSlot(i, addItem(target, source.getStackInSlot(i)));
        }
    }
    
    public static ItemStack addItem(IItemHandler target, ItemStack remaining) {
        // Try to insert item into all slots
        for (int i = 0; i < target.getSlots(); i++) {
            remaining = target.insertItem(i, remaining, false);
            if (remaining.isEmpty()) { break; }
        }
        return remaining;
    }
}
