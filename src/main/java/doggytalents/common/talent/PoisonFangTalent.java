package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
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

public class PoisonFangTalent extends TalentInstance {

    public PoisonFangTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (dogIn.isTamed()) {
            if (this.level() < 5) {
                return ActionResultType.PASS;
            }

            ItemStack stack = playerIn.getHeldItem(handIn);

            if (stack.getItem() == Items.SPIDER_EYE) {

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
    public ActionResultType isPotionApplicable(AbstractDogEntity dogIn, EffectInstance effectIn) {
        if (this.level() >= 3) {
            if (effectIn.getPotion() == Effects.POISON) {
                return ActionResultType.FAIL;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType attackEntityAsMob(AbstractDogEntity dog, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (this.level() > 0) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, this.level() * 20, 0));
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }
}
