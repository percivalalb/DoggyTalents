package doggytalents.talent;

import java.util.List;
import java.util.Random;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;

/**
 * @author ProPercivalalb
 */
public class DoggyTorch extends ITalent {
	
	private ItemStack findTorch(InventoryPackPuppy inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.TORCH))
            {
                return itemstack;
            }
        }

        return ItemStack.EMPTY;
    }

	@Override
	public void onUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");		

    	if(dog.isTamed() && level > 0 && dog.getHealth() > 1) {
			if(dog.world.getLight(dog.getPosition()) < 8 && dog.world.isBlockNormalCube(dog.getPosition().down(), false)) {    				
				ItemStack torchStack = this.findTorch(inventory);
				if(level > 4) {
					dog.world.setBlockState(dog.getPosition(), Blocks.TORCH.getDefaultState());
				} 
				else {
					if(!torchStack.isEmpty()) {				        
		                if(level < 5) {
		                	torchStack.shrink(1);
		                	dog.world.setBlockState(dog.getPosition(), Blocks.TORCH.getDefaultState());
		                }

	                    if(torchStack.isEmpty()) {
	                        inventory.addItem(ItemStack.EMPTY);
	                    }
				    }
				}				
			}
		}
	}	
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		if(dog.isTamed() && level > 0 && dog.getHealth() > 1) {
			List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getEntityBoundingBox().grow(1D, 1D, 1D));
	        for(EntityItem entityItem : list) {
	            if(entityItem.isDead || DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entityItem.getItem())) continue;
	            
	            if(entityItem.getItem().getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
		            ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getItem());
		            if(!itemstack1.isEmpty())
		            	entityItem.setItem(itemstack1);
		            else {
		                entityItem.setDead();
		                dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		            }
	            }		            
	        }
		}
	}
	
	@Override
	public String getKey() {
		return "doggytorch";
	}

}
