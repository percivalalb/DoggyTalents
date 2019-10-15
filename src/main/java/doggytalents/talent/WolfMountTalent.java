package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class WolfMountTalent extends Talent {

    @Override
    public EnumActionResult onInteract(IDogEntity dog, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(stack.isEmpty() && dog.canInteract(player)) {
            if(dog.getTalentFeature().getLevel(this) > 0 && player.getRidingEntity() == null && !player.onGround && !dog.getHungerFeature().isIncapacicated()) {
                if(!dog.world.isRemote) {
                    dog.getAISit().setSitting(false);
                    player.rotationYaw = dog.rotationYaw;
                    player.rotationPitch = dog.rotationPitch;

                    if(!dog.world.isRemote) {
                        player.startRiding(dog);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void livingTick(IDogEntity dog) {
        if(dog.isBeingRidden() && (dog.getHungerFeature().getDogHunger() <= 0 || dog.getHungerFeature().isIncapacicated())) {
            dog.getControllingPassenger().sendMessage(new TextComponentTranslation("talent.doggytalents.wolf_mount.exhausted", dog.getName()));

            dog.removePassengers();
        }
    }

    @Override
    public int onHungerTick(IDogEntity dog, int totalInTick) {
        if(dog.getControllingPassenger() instanceof EntityPlayer) {
            totalInTick += dog.getTalentFeature().getLevel(this) < 5 ? 3 : 1;
        }

        return totalInTick;
    }

    @Override
    public ActionResult<Integer> fallProtection(IDogEntity dog) {
        if(dog.getTalentFeature().getLevel(this) == 5)
            return ActionResult.newResult(EnumActionResult.SUCCESS, 1);

        return ActionResult.newResult(EnumActionResult.PASS, 0);
    }

    @Override
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) {
        Entity entity = damageSource.getTrueSource();
        return dog.isBeingRidden() && entity != null && dog.isRidingOrBeingRiddenBy(entity) ? false : true;
    }
}
