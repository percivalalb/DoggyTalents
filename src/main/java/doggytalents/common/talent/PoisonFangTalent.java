package doggytalents.common.talent;

import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PoisonFangTalent extends Talent {

    @Override
    public ActionResultType processInteract(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        int level = dogIn.getLevel(this);

        if (dogIn.isTamed()) {
            if (level < 5) {
                return ActionResultType.PASS;
            }

            ItemStack stack = playerIn.getHeldItem(handIn);

            if(stack.getItem() == Items.SPIDER_EYE) {

                if (playerIn.getActivePotionEffect(Effects.POISON) == null || dogIn.getDogHunger() < 30) {
                    return ActionResultType.FAIL;
                }

                if (!worldIn.isRemote) {
                    playerIn.clearActivePotions();
                    dogIn.setDogHunger(dogIn.getDogHunger() - 30);
                    dogIn.consumeItemFromStack(playerIn, stack);
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType isPotionApplicable(DogEntity dogIn, EffectInstance effectIn) {
        if(dogIn.getLevel(this) >= 3) {
            if(effectIn.getPotion() == Effects.POISON) {
                return ActionResultType.FAIL;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType attackEntityAsMob(DogEntity dog, Entity entity) {
        if(entity instanceof LivingEntity) {
            int level = dog.getLevel(this);

            if (level > 0) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, level * 20, 0));
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }
}
