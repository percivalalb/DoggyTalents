package doggytalents.helper;

import doggytalents.ModItems;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public class DogUtil {

	public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigate pathfinder) {
    	int i = ObjectLib.BRIDGE.floor(owner.posX) - 2;
        int j = ObjectLib.BRIDGE.floor(owner.posZ) - 2;
        int k = ObjectLib.BRIDGE.floor(owner.getEntityBoundingBox().minY);

        for(int l = 0; l <= 4; ++l) {
            for(int i1 = 0; i1 <= 4; ++i1) {
                if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && ObjectLib.METHODS.isTeleportFriendlyBlock(entity, world, i, j, k, l, i1)) {
                	entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), entity.rotationYaw, entity.rotationPitch);
                    pathfinder.clearPathEntity();
                    return;
                }
            }
        }
    }
    
    public static ItemStack feedDog(EntityDog dog, IInventory inventory, int slotIndex) {
        if(!ObjectLib.STACK_UTIL.isEmpty(inventory.getStackInSlot(slotIndex))) {
            ItemStack itemstack = inventory.getStackInSlot(slotIndex);
            dog.setDogHunger(dog.getDogHunger() + dog.foodValue(itemstack));
            
            if(itemstack.getItem() == ModItems.CHEW_STICK) { //TODO add player paramater
            	((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(null, dog);
            }

            if(ObjectLib.STACK_UTIL.getCount(inventory.getStackInSlot(slotIndex)) <= 1) {
                ItemStack itemstack1 = inventory.getStackInSlot(slotIndex);
                inventory.setInventorySlotContents(slotIndex, ObjectLib.STACK_UTIL.getEmpty());
                return itemstack1;
            }

            ItemStack itemstack2 = inventory.getStackInSlot(slotIndex).splitStack(1);

            if(ObjectLib.STACK_UTIL.isEmpty(inventory.getStackInSlot(slotIndex)))
            	inventory.setInventorySlotContents(slotIndex, ObjectLib.STACK_UTIL.getEmpty());
            else
            	inventory.markDirty();

            return itemstack2;
        }
        else
            return ObjectLib.STACK_UTIL.getEmpty();
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
    	if(ObjectLib.STACK_UTIL.isEmpty(stack)) return ObjectLib.STACK_UTIL.getEmpty();
    	
        ItemStack itemstack = stack.copy();

        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemstack1 = inventory.getStackInSlot(i);

            if(ObjectLib.STACK_UTIL.isEmpty(itemstack1)) {
            	inventory.setInventorySlotContents(i, itemstack);
            	inventory.markDirty();
                return ObjectLib.STACK_UTIL.getEmpty();
            }

            if(ItemStack.areItemsEqual(itemstack1, itemstack)) {
                int j = Math.min(inventory.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(ObjectLib.STACK_UTIL.getCount(itemstack), j - ObjectLib.STACK_UTIL.getCount(itemstack1));

                if(k > 0) {
                	ObjectLib.STACK_UTIL.grow(itemstack1, k);
                	ObjectLib.STACK_UTIL.shrink(itemstack, k);

                    if(ObjectLib.STACK_UTIL.isEmpty(itemstack)) {
                    	inventory.markDirty();
                        return ObjectLib.STACK_UTIL.getEmpty();
                    }
                }
            }
        }

        if(ObjectLib.STACK_UTIL.getCount(itemstack) != ObjectLib.STACK_UTIL.getCount(stack))
        	inventory.markDirty();

        return itemstack;
    }
}
