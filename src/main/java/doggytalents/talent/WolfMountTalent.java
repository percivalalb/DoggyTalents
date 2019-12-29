package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author ProPercivalalb
 */
public class WolfMountTalent extends Talent {

    @Override
    public ActionResultType onInteract(IDogEntity dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if(stack.isEmpty() && dogIn.canInteract(playerIn)) {
            if(dogIn.getTalentFeature().getLevel(this) > 0 && playerIn.getRidingEntity() == null && !playerIn.onGround && !dogIn.getHungerFeature().isIncapacicated()) {
                if(!dogIn.world.isRemote) {
                    dogIn.getAISit().setSitting(false);
                    playerIn.rotationYaw = dogIn.rotationYaw;
                    playerIn.rotationPitch = dogIn.rotationPitch;

                    if(!dogIn.world.isRemote) {
                        playerIn.startRiding(dogIn);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void livingTick(IDogEntity dog) {
        if(dog.isBeingRidden() && (dog.getHungerFeature().getDogHunger() <= 0 || dog.getHungerFeature().isIncapacicated())) {
            dog.getControllingPassenger().sendMessage(new TranslationTextComponent("talent.doggytalents.wolf_mount.exhausted", dog.getName()));

            dog.removePassengers();
        }
    }

    @Override
    public int onHungerTick(IDogEntity dog, int totalInTick) {
        if(dog.getControllingPassenger() instanceof PlayerEntity) {
            totalInTick += dog.getTalentFeature().getLevel(this) < 5 ? 3 : 1;
        }

        return totalInTick;
    }

    @Override
    public ActionResult<Integer> fallProtection(IDogEntity dog) {
        if(dog.getTalentFeature().getLevel(this) == 5)
            return ActionResult.func_226248_a_(1);

        return ActionResult.func_226250_c_(0);
    }

    @Override
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) {
        Entity entity = damageSource.getTrueSource();
        return dog.isBeingRidden() && entity != null && dog.isRidingOrBeingRiddenBy(entity) ? false : true;
    }
}
