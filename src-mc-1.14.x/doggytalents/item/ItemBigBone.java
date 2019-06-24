package doggytalents.item;

import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemBigBone extends Item implements IDogInteractItem {
	
	public ItemBigBone(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onInteractWithDog(EntityDog dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(dogIn.getGrowingAge() >= 0) {
			if(!playerIn.abilities.isCreativeMode)
				playerIn.getHeldItem(handIn).shrink(1);

			if(!playerIn.world.isRemote) {
				dogIn.setDogSize(dogIn.getDogSize() + 1);
			}
			return ActionResultType.SUCCESS;
		}
		else {
			if(!playerIn.world.isRemote){
				playerIn.sendMessage(new TranslationTextComponent("treat.big_bone.too_young"));
			}
			return ActionResultType.FAIL;
		}
	}
}