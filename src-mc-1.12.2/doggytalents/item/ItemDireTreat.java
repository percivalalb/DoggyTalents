package doggytalents.item;

import doggytalents.api.IDogTreat;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

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
				stack.shrink(1);

			if(!player.world.isRemote) {
	            dog.levels.increaseDireLevel();
	            dog.setHealth(dog.getMaxHealth());
	            dog.getSitAI().setSitting(true);
	            dog.getNavigator().clearPath();
	            dog.world.setEntityState(dog, (byte)7);
	            dog.playTameEffect(true);
	            if (!player.world.isRemote)
	            	player.sendMessage(new TextComponentTranslation("dogtreat.levelup"));
			}
		}
		else if(type == EnumFeedBack.TOOYOUNG) {
			if (!player.world.isRemote){
				 dog.playTameEffect(false);
				 player.sendMessage(new TextComponentTranslation("dogtreat.tooyoung"));
			}
		}
		else if(type == EnumFeedBack.LEVELTOOHIGH) {
            player.world.setEntityState(dog, (byte)6);
            if (!player.world.isRemote) {
            	 dog.playTameEffect(false);
            	 player.sendMessage(new TextComponentTranslation("dogtreat.toomuch"));
            }
		}
		else if(type == EnumFeedBack.COMPLETE) {
            player.world.setEntityState(dog, (byte)6);
            if (!player.world.isRemote) {
            	 player.sendMessage(new TextComponentTranslation("dogtreat.ultimatelevel"));
            }
		}
	}

}
