package doggytalents.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import doggytalents.api.IDogTreat;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.DogTalents;

/**
 * @author ProPercivalalb
 **/
public class ItemTreat extends ItemDT implements IDogTreat {

	private int maxLevel;
	
	public ItemTreat(int id, String iconPath, int level) {
		super(id, iconPath);
		this.maxLevel = level;
	}

	@Override
	public EnumFeedBack canGiveToDog(EntityPlayer player, EntityDTDoggy dog, int level, int direLevel) {
		if (level < maxLevel && dog.getGrowingAge() >= 0) {
			return EnumFeedBack.JUSTRIGHT;
        }
		else if (dog.getGrowingAge() < 0) {
			return EnumFeedBack.TOOYOUNG;
		}
		else {
			return EnumFeedBack.LEVELTOOHIGH;
		}
	}

	@Override
	public void giveTreat(EnumFeedBack type, EntityPlayer player, EntityDTDoggy dog, DogTalents skills) {
		ItemStack itemstack = player.getCurrentEquippedItem();
		
		if(type == EnumFeedBack.JUSTRIGHT) {
			if(!player.capabilities.isCreativeMode) {
				itemstack.stackSize--;
			}

            if (itemstack.stackSize <= 0) {
            	player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            dog.level.increaseLevel(1);
            dog.setHealth(dog.getMaxHealth());
            dog.getSitAI().setSitting(true);
            dog.worldObj.setEntityState(dog, (byte)7);
            dog.playTameEffect(true);
            if (!player.worldObj.isRemote) {
            	player.addChatMessage("Level up!");
            }
		}
		else if(type == EnumFeedBack.TOOYOUNG) {
			if (!player.worldObj.isRemote){
				 dog.playTameEffect(false);
	            player.addChatMessage("This dog's a bit too young to be learning skills just yet.");
			}
		}
		else if(type == EnumFeedBack.LEVELTOOHIGH) {
            player.worldObj.setEntityState(dog, (byte)6);
            if (!player.worldObj.isRemote) {
            	dog.playTameEffect(false);
            	player.addChatMessage((new StringBuilder()).append("Your dog is too high level to gain anything from these treats.").toString());
            }
		}
	}

}
