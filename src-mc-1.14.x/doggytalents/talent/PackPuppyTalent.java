package doggytalents.talent;

import java.util.List;
import java.util.function.Predicate;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;

/**
 * @author ProPercivalalb
 */
public class PackPuppyTalent extends Talent {

	public static Predicate<ItemEntity> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
		return entity.isAlive() && !DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entity.getItem());
	};
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("packpuppyinventory", new InventoryPackPuppy(dog));
	}
	
	@Override
	public void writeAdditional(EntityDog dog, CompoundNBT tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.writeToNBT(tagCompound);
	}
	
	@Override
	public void readAdditional(EntityDog dog, CompoundNBT tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.readFromNBT(tagCompound);
	}
	
	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, PlayerEntity player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if(dog.isTamed()) {
	    	if(level > 0) {
	    		if(stack.isEmpty()) {
	    			
	    			if(player.isSneaking() && !player.world.isRemote && dog.canInteract(player)) {
	    				INamedContainerProvider inventory = (INamedContainerProvider)dog.objects.get("packpuppyinventory");
	    
	    				if(inventory != null) {
	    	                if(player instanceof ServerPlayerEntity && !(player instanceof FakePlayer)) {
	    	                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)player;
	    	                    entityPlayerMP.openContainer(inventory);
	    	                }
	    	            }
	    				dog.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, dog.world.rand.nextFloat() * 0.1F + 0.9F);
	    				return ActionResult.newResult(ActionResultType.SUCCESS, stack);
	    			}
	    		}
	    	}
	    }
		
		return ActionResult.newResult(ActionResultType.PASS, stack);
	}
	
	@Override
	public void livingTick(EntityDog dog) {
		if(!dog.world.isRemote && dog.TALENTS.getLevel(this) >= 5 && dog.getHealth() > 1) {
			InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
			
			List<ItemEntity> list = dog.world.getEntitiesWithinAABB(ItemEntity.class, dog.getBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);
	        for(ItemEntity entityItem : list) {
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
