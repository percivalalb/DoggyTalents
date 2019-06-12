package doggytalents.talent;

import java.util.List;
import java.util.function.Predicate;

import doggytalents.ModTags;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author ProPercivalalb
 */
public class PackPuppyTalent extends Talent {

	public static Predicate<EntityItem> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
		return entity.isAlive() && !entity.getItem().getItem().isIn(ModTags.PACK_PUPPY_BLACKLIST);
	};
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("packpuppyinventory", new InventoryPackPuppy(dog));
	}
	
	@Override
	public void writeAdditional(EntityDog dog, NBTTagCompound tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.writeToNBT(tagCompound);
	}
	
	@Override
	public void readAdditional(EntityDog dog, NBTTagCompound tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.readFromNBT(tagCompound);
	}
	
	@Override
	public void onLevelReset(EntityDog dog, int preLevel) {
		// No need to drop anything if dog didn't have pack puppy
		if(preLevel > 0) {
			InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
			InventoryHelper.dropInventoryItems(dog.world, dog, inventory);
			inventory.clear();
		}
	}
	
	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if(dog.isTamed()) {
	    	if(level > 0) {
	    		if(stack.isEmpty()) {
	    			
	    			if(player.isSneaking() && !player.world.isRemote && dog.canInteract(player)) {
	    				IInteractionObject inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
	    
	    				if(inventory != null) {
	    	                if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
	    	                	
	    	                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;

	    	                    NetworkHooks.openGui(entityPlayerMP, inventory, buf -> buf.writeInt(dog.getEntityId()));
	    	                }
	    	            }
	    				dog.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, dog.world.rand.nextFloat() * 0.1F + 0.9F);
	    				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	    			}
	    		}
	    	}
	    }
		
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
	
	@Override
	public void livingTick(EntityDog dog) {
		if(!dog.world.isRemote && dog.TALENTS.getLevel(this) >= 5 && dog.getHealth() > 1) {
			InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
			
			List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);
	        for(EntityItem entityItem : list) {
	            ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getItem());

	            if(!itemstack1.isEmpty())
	            	entityItem.setItem(itemstack1);
	            else {
	                entityItem.remove();
	                dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	            }
	        }
		}
	}
}
