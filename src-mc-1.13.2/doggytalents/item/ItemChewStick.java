package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public class ItemChewStick extends Item {
	
	public ItemChewStick(Properties properties) {
		super(properties);
	}

	public void addChewStickEffects(EntityDog dog) {
		dog.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 1, false, true));
		dog.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 6, false, true));
		dog.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 2, false, true));
	}
}
