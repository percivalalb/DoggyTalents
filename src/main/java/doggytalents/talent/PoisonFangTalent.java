package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ProPercivalalb
 */
public class PoisonFangTalent extends Talent {

    @Override
    public EnumActionResult onInteract(IDogEntity dog, EntityPlayer player, EnumHand hand) {
        int level = dog.getTalentFeature().getLevel(this);

        if (dog.isTamed()) {

            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == Items.SPIDER_EYE && !player.world.isRemote && dog.getHungerFeature().getDogHunger() > 30) {
                if(!dog.world.isRemote) {
                    player.clearActivePotions();
                }

                dog.getHungerFeature().setDogHunger(dog.getHungerFeature().getDogHunger() - 30);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult isPostionApplicable(IDogEntity dog, PotionEffect potionEffect) {
        if(this.getLevel(dog) >= 3) {
            if(potionEffect.getPotion() == MobEffects.POISON) {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) {
        int level = dog.getTalentFeature().getLevel(this);

        if(entity instanceof EntityLivingBase && level > 0)
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.POISON, level * 20, 0));

        return damage;
    }
}
