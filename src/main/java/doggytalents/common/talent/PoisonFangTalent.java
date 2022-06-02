package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PoisonFangTalent extends TalentInstance {

    public PoisonFangTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public InteractionResult processInteract(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        if (dogIn.isTame()) {
            if (this.level() < 5) {
                return InteractionResult.PASS;
            }

            ItemStack stack = playerIn.getItemInHand(handIn);

            if (stack.getItem() == Items.SPIDER_EYE) {

                if (playerIn.getEffect(MobEffects.POISON) == null || dogIn.getDogHunger() < 30) {
                    return InteractionResult.FAIL;
                }

                if (!worldIn.isClientSide) {
                    playerIn.removeAllEffects();
                    dogIn.setDogHunger(dogIn.getDogHunger() - 30);
                    dogIn.consumeItemFromStack(playerIn, stack);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult isPotionApplicable(AbstractDog dogIn, MobEffectInstance effectIn) {
        if (this.level() >= 3) {
            if (effectIn.getEffect() == MobEffects.POISON) {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult attackEntityAsMob(AbstractDog dog, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (this.level() > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, this.level() * 20, 0));
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }
}
