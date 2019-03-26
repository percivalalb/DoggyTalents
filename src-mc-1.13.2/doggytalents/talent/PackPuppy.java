package doggytalents.talent;

import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author ProPercivalalb
 */
public class PackPuppy extends ITalent {

	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("packpuppyinventory", new InventoryPackPuppy(dog));
	}
	
	@Override
	public void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.writeToNBT(tagCompound);
	}
	
	@Override
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		inventory.readFromNBT(tagCompound);
	}
	
	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		
	    if(dog.isTamed()) {
	    	if(level > 0) {
	    		if(!stack.isEmpty()) {
	    			
	    			if(stack.getItem().equals(Item.getItemFromBlock(Blocks.OAK_PLANKS)) && !player.world.isRemote && dog.canInteract(player)) {
	    				IInteractionObject inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
	    
	    				if(inventory != null) {
	    	                if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
	    	                	
	    	                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;

	    	                    NetworkHooks.openGui(entityPlayerMP, inventory, buf -> buf.writeInt(dog.getEntityId()));
	    	                }
	    	            }
	    				dog.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, dog.world.rand.nextFloat() * 0.1F + 0.9F);
	    				return true;
	    			}
	    		}
	    	}
	    }
		
		return false;
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(dog.isServer() && dog.TALENTS.getLevel(this) >= 5 && dog.getHealth() > 1) {
			InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
			
			List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getBoundingBox().grow(2.5D, 1D, 2.5D));
	        for(EntityItem entityItem : list) {
	            if(!entityItem.isAlive() || DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entityItem.getItem())) continue;
	            
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
	
	@Override
	public String getKey() {
		return "packpuppy";
	}

}
