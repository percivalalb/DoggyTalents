package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

/**
 * @author ProPercivalalb
 */
public class PoisonFangTalent extends Talent {

    @Override
    public ActionResultType onInteract(IDogEntity dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        int level = dogIn.getTalentFeature().getLevel(this);

        if (dogIn.isTamed() && level == 5) {
            if(stack.getItem() == Items.SPIDER_EYE && dogIn.getHungerFeature().getDogHunger() > 30) {
                playerIn.clearActivePotions();
                dogIn.getHungerFeature().setDogHunger(dogIn.getHungerFeature().getDogHunger() - 30);

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public boolean isPostionApplicable(IDogEntity dog, EffectInstance potionEffect) {
        if(dog.getTalentFeature().getLevel(this) >= 3) {
            if(potionEffect.getPotion() == Effects.POISON) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) {
        int level = dog.getTalentFeature().getLevel(this);

        if(entity instanceof LivingEntity && level > 0) {
            ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.POISON, level * 20, 0));
        }

        return damage;
    }
}
