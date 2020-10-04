package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WolfMountTalent extends Talent {

    private static final UUID WOLF_MOUNT_JUMP = UUID.fromString("7f338124-f223-4630-8515-70ee0bfbc653");

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(AbstractDogEntity.JUMP_STRENGTH, WOLF_MOUNT_JUMP, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(AbstractDogEntity.JUMP_STRENGTH, WOLF_MOUNT_JUMP, this::createSpeedModifier);
    }

    @Override
    public void removed(AbstractDogEntity dogIn, int preLevel) {
        dogIn.removeAttributeModifier(AbstractDogEntity.JUMP_STRENGTH, WOLF_MOUNT_JUMP);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            double speed = 0.06D * level;

            if (level >= 5) {
                speed += 0.04D;
            }

            return new AttributeModifier(uuidIn, "Wolf Mount", speed, AttributeModifier.Operation.ADDITION).setSaved(false);
        }

        return null;
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.isEmpty()) { // Held item
            if (dogIn.canInteract(playerIn) && dogIn.getLevel(this) > 0) { // Dog
                if (playerIn.getRidingEntity() == null && !playerIn.onGround) { // Player
                    if (!dogIn.world.isRemote) {
                        dogIn.getAISit().setSitting(false);
                        playerIn.rotationYaw = dogIn.rotationYaw;
                        playerIn.rotationPitch = dogIn.rotationPitch;
                        playerIn.startRiding(dogIn);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void livingTick(AbstractDogEntity dog) {
        if (dog.isBeingRidden() && dog.getDogHunger() < 1) {
            dog.getControllingPassenger().sendMessage(new TranslationTextComponent("talent.doggytalents.wolf_mount.exhausted", dog.getName()));

            dog.removePassengers();
        }
    }

    @Override
    public ActionResult<Integer> hungerTick(AbstractDogEntity dogIn, int hungerTick) {
        if (dogIn.canPassengerSteer()) {
            hungerTick += dogIn.getLevel(this) < 5 ? 3 : 1;
            return ActionResult.resultSuccess(hungerTick);
        }

        return ActionResult.resultPass(hungerTick);
    }

    @Override
    public ActionResult<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        if (dogIn.getLevel(this) >= 5) {
            return ActionResult.resultSuccess(distance - 1F);
        }

        return ActionResult.resultPass(0F);
    }

    @Override
    public ActionResultType hitByEntity(AbstractDogEntity dogIn, Entity entity) {
        // If the attacking entity is riding block
        return dogIn.isRidingOrBeingRiddenBy(entity) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }
}
