package doggytalents.talent;

import java.util.List;

import doggytalents.DoggyTalents;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import doggytalents.proxy.CommonProxy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

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
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player) {
		ItemStack stack = player.inventory.getCurrentItem();

	    if (dog.isTamed()) {
	    	if (stack != null) {
	    		if(stack.getItem() == Item.getItemFromBlock(Blocks.planks) && !player.worldObj.isRemote) {
	    			player.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_PACKPUPPY, dog.worldObj, dog.getEntityId(), 0, 0);
	    			dog.worldObj.playSoundEffect(dog.posX, dog.posY + 0.5D, dog.posZ, "random.chestopen", 0.5F, dog.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                	return true;
	    		}
	    	}
	    }
		
		return false;
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(!dog.worldObj.isRemote && dog.talents.getLevel(this) >= 5 && dog.getHealth() > 1) {
			InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
			
			List list = dog.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(dog.posX - 2.5D, dog.posY - 1.0D, dog.posZ - 2.5D, dog.posX + 2.5D, dog.posY + 1.0D, dog.posZ + 2.5D));
	        for(int i = 0; i < list.size(); i++) {
	            EntityItem entityItem = (EntityItem)list.get(i);
	            if(entityItem.isDead || DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entityItem.getEntityItem())) continue;
	            
	            ItemStack itemstack = entityItem.getEntityItem().copy();
	            ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getEntityItem());

	            if(itemstack1 != null && itemstack1.stackSize != 0)
	            	entityItem.setEntityItemStack(itemstack1);
	            else {
	                entityItem.setDead();
	                dog.worldObj.playSoundAtEntity(dog, "random.pop", 0.2F, ((dog.getRNG().nextFloat() - dog.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
	            }
	        }
		}
	}
	
	@Override
	public String getKey() {
		return "packpuppy";
	}

}
