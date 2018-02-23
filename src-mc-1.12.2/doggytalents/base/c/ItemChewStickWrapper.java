package doggytalents.base.c;

import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ItemChewStickWrapper extends ItemChewStick {

	@Override
	public void addChewStickEffects(EntityPlayer player, EntityDog dog) {
		dog.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 1, false, true));
		dog.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 6, false, true));
		dog.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 2, false, true));
	}

}
