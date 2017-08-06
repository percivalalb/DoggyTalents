package doggytalents.helper;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class DogUtil {

	public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigate pathfinder) {
    	int i = MathHelper.floor_double(owner.posX) - 2;
        int j = MathHelper.floor_double(owner.posZ) - 2;
        int k = MathHelper.floor_double(owner.getEntityBoundingBox().minY);

        for(int l = 0; l <= 4; ++l) {
            for(int i1 = 0; i1 <= 4; ++i1) {
                if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && isTeleportFriendlyBlock(entity, world, i, j, k, l, i1)) {
                	entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), entity.rotationYaw, entity.rotationPitch);
                    pathfinder.clearPathEntity();
                    return;
                }
            }
        }
    }
	
	public static boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return World.doesBlockHaveSolidTopSurface(world, new BlockPos(xBase + xAdd, y - 1, zBase + zAdd)) && isEmptyBlock(world, new BlockPos(xBase + xAdd, y, zBase + zAdd)) && isEmptyBlock(world, new BlockPos(xBase + xAdd, y + 1, zBase + zAdd));
	}

	private static boolean isEmptyBlock(World world, BlockPos pos) {
		IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block == Blocks.air ? true : !block.isFullCube();
    }
	
    public static ItemStack feedDog(EntityDog dog, IInventory inventory, int slotIndex) {
        if(inventory.getStackInSlot(slotIndex) != null) {
            ItemStack itemstack = inventory.getStackInSlot(slotIndex);
            dog.setDogHunger(dog.getDogHunger() + dog.foodValue(itemstack));
            
            if(itemstack.getItem() == ModItems.CHEW_STICK) {
            	//dog.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 1, false, true));
            	dog.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 200, 6, false, true));
            	dog.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 100, 2, false, true));
            }

            if(inventory.getStackInSlot(slotIndex).stackSize <= 1) {
                ItemStack itemstack1 = inventory.getStackInSlot(slotIndex);
                inventory.setInventorySlotContents(slotIndex, null);
                return itemstack1;
            }

            ItemStack itemstack2 = inventory.getStackInSlot(slotIndex).splitStack(1);

            if(inventory.getStackInSlot(slotIndex) == null)
            	inventory.setInventorySlotContents(slotIndex, null);
            else
            	inventory.markDirty();

            return itemstack2;
        }
        else
            return null;
    }
    
    public static boolean doesInventoryContainFood(EntityDog dog, IInventory inventory) {
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            if(dog.foodValue(inventory.getStackInSlot(i)) > 0)
            	return true;
        }

        return false;
    }
    
    public static int getFirstSlotWithFood(EntityDog dog, IInventory inventory) {
    	 for(int i = 0; i < inventory.getSizeInventory(); i++) {
             if(dog.foodValue(inventory.getStackInSlot(i)) > 0)
             	return i;
         }

        return -1;
    }
    
    public static ItemStack addItem(IInventory inventory, ItemStack stack) {
    	if(stack == null) return null;
    	
        ItemStack itemstack = stack.copy();
        
        

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inventory.getStackInSlot(i);

            if (itemstack1 == null) {
            	inventory.setInventorySlotContents(i, itemstack);
            	inventory.markDirty();
                return null;
            }

            if (ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                int j = Math.min(inventory.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);

                if (k > 0) {
                    itemstack1.stackSize += k;
                    itemstack.stackSize -= k;

                    if (itemstack.stackSize == 0) {
                    	inventory.markDirty();
                        return null;
                    }
                }
            }
        }

        if(itemstack.stackSize != stack.stackSize)
        	inventory.markDirty();

        return itemstack;
    }
}
