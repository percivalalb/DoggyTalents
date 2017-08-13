package doggytalents.base.b;

import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemChewStickWrapper extends ItemChewStick {

	@Override
	public void addChewStickEffects(EntityPlayer player, EntityDog dog) {
		dog.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 200, 6, false, true));
		dog.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 100, 2, false, true));
	}

}
