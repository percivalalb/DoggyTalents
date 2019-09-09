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
        dog.addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 1, false, true));
        dog.addPotionEffect(new EffectInstance(Effects.SPEED, 200, 6, false, true));
        dog.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 2, false, true));
    }
}
