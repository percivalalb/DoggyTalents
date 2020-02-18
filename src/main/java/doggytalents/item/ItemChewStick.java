package doggytalents.item;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.IDogFedCallback;
import doggytalents.api.inferface.IDogFoodItem;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ItemChewStick extends Item implements IDogFoodItem {
    
    public ItemChewStick(Properties properties) {
        super(properties);
    }

    @Override
    public void onItemConsumed(IDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        dogIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 1, false, true));
        dogIn.addPotionEffect(new EffectInstance(Effects.SPEED, 200, 6, false, true));
        dogIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 2, false, true));
    }

    @Override
    public int getFoodValue(IDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        return 10;
    }
}
