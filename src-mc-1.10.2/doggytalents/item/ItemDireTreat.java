package doggytalents.item;

import doggytalents.api.IDogTreat;
import doggytalents.entity.EntityDog;
import doggytalents.helper.ChatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class ItemDireTreat extends ItemDT implements IDogTreat {
	
	public ItemDireTreat() {
		super();
	}

	@Override
	public EnumFeedBack canGiveToDog(EntityPlayer player, EntityDog dog, int level, int direLevel) {
		if (level >= 60 && direLevel < 30 && dog.getGrowingAge() >= 0) {
			return EnumFeedBack.JUSTRIGHT;
        }
		else if (dog.getGrowingAge() < 0) {
			return EnumFeedBack.TOOYOUNG;
		}
		else if(level < 60) {
			return EnumFeedBack.LEVELTOOHIGH;
		}
		else {
			return EnumFeedBack.COMPLETE;
		}
	}

	@Override
	public void giveTreat(EnumFeedBack type, EntityPlayer player, ItemStack stack, EntityDog dog) {

		if(type == EnumFeedBack.JUSTRIGHT) {
			if(!player.capabilities.isCreativeMode)
				--stack.stackSize;

			if(!player.worldObj.isRemote) {
	            dog.levels.increaseDireLevel();
	            dog.setHealth(dog.getMaxHealth());
	            dog.getSitAI().setSitting(true);
	            dog.getNavigator().clearPathEntity();
	            dog.worldObj.setEntityState(dog, (byte)7);
	            dog.playTameEffect(true);
	            if (!player.worldObj.isRemote)
	            	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogtreat.levelup"));
			}
		}
		else if(type == EnumFeedBack.TOOYOUNG) {
			if (!player.worldObj.isRemote){
				 dog.playTameEffect(false);
				 player.addChatMessage(ChatHelper.getChatComponentTranslation("dogtreat.tooyoung"));
			}
		}
		else if(type == EnumFeedBack.LEVELTOOHIGH) {
            player.worldObj.setEntityState(dog, (byte)6);
            if (!player.worldObj.isRemote) {
            	 dog.playTameEffect(false);
            	 player.addChatMessage(ChatHelper.getChatComponentTranslation("dogtreat.toomuch"));
            }
		}
		else if(type == EnumFeedBack.COMPLETE) {
            player.worldObj.setEntityState(dog, (byte)6);
            if (!player.worldObj.isRemote) {
            	 player.addChatMessage(ChatHelper.getChatComponentTranslation("dogtreat.ultimatelevel"));
            }
		}
	}

}
