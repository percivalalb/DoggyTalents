package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ItemChewStick extends Item {
	
	public ItemChewStick(Properties properties) {
		super(properties);
	}

	public void addChewStickEffects(EntityDog dog) {
		dog.addPotionEffect(new EffectInstance(Effects.field_188423_x, 100, 1, false, true));
		dog.addPotionEffect(new EffectInstance(Effects.field_76424_c, 200, 6, false, true));
		dog.addPotionEffect(new EffectInstance(Effects.field_76428_l, 100, 2, false, true));
	}
}
