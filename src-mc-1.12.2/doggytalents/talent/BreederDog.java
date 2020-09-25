package doggytalents.talent;

import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * @author ProPercivalalb
 */
public class BreederDog extends ITalent {
	
	private ItemStack findBreedingItem(InventoryPackPuppy inventory, EntityAnimal entity)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (entity.isBreedingItem(itemstack))
            {
                return itemstack;
            }
        }

        return ItemStack.EMPTY;
    }
	
	private boolean canBreedAnimal(EntityDog dog, EntityAnimal entity) {
		if(!entity.isInLove() && !entity.isChild() && entity.getLoveCause() != dog.getOwner()) {
			int level = dog.talents.getLevel(this);
			if(level > 0 && (entity instanceof EntityChicken || entity instanceof EntityRabbit)) {
				return true;
			}
			if(level > 1 && (entity instanceof EntityPig || entity instanceof EntitySheep)) {
				return true;
			}
			if(level > 2 && entity instanceof EntityCow) {
				return true;
			}
			if(level > 3 && entity instanceof EntityLlama) {
				return true;
			}
			if(level > 4 && (entity instanceof EntityHorse || entity instanceof EntityDonkey)) {
				return true;
			}
		}
		return false;		
	}
	
	@Override
	public void onUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");

		if(level > 0 && !dog.isSitting()) {
			if(dog.masterOrder() == 5 || dog.mode.isMode(ModeUtil.EnumMode.WANDERING)) {
				double distance = 8D + level*2;
	       	 	List list = dog.world.getEntitiesWithinAABBExcludingEntity(dog, dog.getEntityBoundingBox().grow(distance, distance, distance));

	       	 	for (int count = 0; count < list.size(); count++) {
	       	 		Entity entity = (Entity)list.get(count);
	       	 		if(entity instanceof EntityAnimal && !(entity instanceof EntityDog) && !(entity instanceof EntityWolf)) {
	       	 		ItemStack breedingStack = this.findBreedingItem(inventory, (EntityAnimal)entity);
	       	 		
	   	 				if(this.canBreedAnimal(dog, (EntityAnimal) entity)) {
	   	 					if(!breedingStack.isEmpty()) {	   	 						
    							dog.getNavigator().tryMoveToXYZ(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), 1.0D);
	    						if(dog.getPosition().getX() == entity.getPosition().getX() && dog.getPosition().getY() == entity.getPosition().getY() && dog.getPosition().getZ() == entity.getPosition().getZ()) {	   		            	
	   	 							((EntityAnimal) entity).setInLove((EntityPlayer) dog.getOwner());
	   	 							breedingStack.shrink(1);	   	 							
   	 								
		   	 						if(breedingStack.isEmpty()) {
	                                	inventory.addItem(ItemStack.EMPTY);
		   	 						}
		   	 					} 
	   	 					}
	   	 				}
	       	 		}     
	       	 	}
			}
        }
	}
	
	private boolean breedingItems(Item item) {
		if(item == Items.WHEAT) return true;
		if(item instanceof ItemSeeds) return true;
		if(item == Items.GOLDEN_APPLE) return true;
		if(item == Items.GOLDEN_CARROT) return true;
		if(item == Items.CARROT) return true;
		if(item == Items.POTATO) return true;
		if(item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) return true;
		if(item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) return true;
		return false;
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		if(dog.isTamed() && level > 0 && dog.getHealth() > 1) {
			List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getEntityBoundingBox().grow(1D, 1D, 1D));
	        for(EntityItem entityItem : list) {
	            if(entityItem.isDead || DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entityItem.getItem())) continue;
	            
	            if(this.breedingItems(entityItem.getItem().getItem())) {
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
		return "breederdog";
	}

}
