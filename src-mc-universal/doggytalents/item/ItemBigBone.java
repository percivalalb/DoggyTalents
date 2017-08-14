package doggytalents.item;

import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBigBone extends ItemDT implements IDogTreat {

	public ItemBigBone() {
        super();
    }
	
	@Override
	public EnumFeedBack canGiveToDog(EntityPlayer player, EntityDog dog, int level, int direLevel) {
		if(dog.getGrowingAge() >= 0)
			return EnumFeedBack.JUSTRIGHT;
		else
			return EnumFeedBack.TOOYOUNG;	
	}

	@Override
	public void giveTreat(EnumFeedBack type, EntityPlayer player, ItemStack stack, EntityDog dog) {
		if(type == EnumFeedBack.JUSTRIGHT) {
			if(!player.capabilities.isCreativeMode)
				ObjectLib.STACK_UTIL.shrink(stack, 1);

			if(!player.world.isRemote) {
				dog.setDogSize(dog.getDogSize() + 1);
			}
		}
		else if(type == EnumFeedBack.TOOYOUNG) {
			if(!player.world.isRemote){
				ObjectLib.BRIDGE.addTranslatedMessage(player, "dogtreat.tooyoung");
			}
		}
	}
}