package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.DoggyAttributes;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WolfMountTalent extends TalentInstance {

    private static final UUID WOLF_MOUNT_JUMP = UUID.fromString("7f338124-f223-4630-8515-70ee0bfbc653");

    public WolfMountTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(DoggyAttributes.JUMP_POWER.get(), WOLF_MOUNT_JUMP, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(DoggyAttributes.JUMP_POWER.get(), WOLF_MOUNT_JUMP, this::createSpeedModifier);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        if (this.level() > 0) {
            double speed = 0.06D * this.level();

            if (this.level() >= 5) {
                speed += 0.04D;
            }

            return new AttributeModifier(uuidIn, "Wolf Mount", speed, AttributeModifier.Operation.ADDITION);
        }

        return null;
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        if (stack.isEmpty()) { // Held item
            if (dogIn.canInteract(playerIn) && this.level() > 0) { // Dog
                if (playerIn.getVehicle() == null && !playerIn.isOnGround()) { // Player
                    if (!dogIn.level.isClientSide) {
                        dogIn.setOrderedToSit(false);
                        playerIn.yRot = dogIn.yRot;
                        playerIn.xRot = dogIn.xRot;
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
        if (dog.isVehicle() && dog.getDogHunger() < 1) {
            dog.getControllingPassenger().sendMessage(new TranslationTextComponent("talent.doggytalents.wolf_mount.exhausted", dog.getName()), dog.getUUID());

            dog.ejectPassengers();
        }
    }

    @Override
    public ActionResult<Integer> hungerTick(AbstractDogEntity dogIn, int hungerTick) {
        if (dogIn.isControlledByLocalInstance()) {
            hungerTick += this.level() < 5 ? 3 : 1;
            return ActionResult.success(hungerTick);
        }

        return ActionResult.pass(hungerTick);
    }

    @Override
    public ActionResult<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        if (this.level() >= 5) {
            return ActionResult.success(distance - 1F);
        }

        return ActionResult.pass(0F);
    }

    @Override
    public ActionResultType hitByEntity(AbstractDogEntity dogIn, Entity entity) {
        // If the attacking entity is riding block
        return dogIn.isPassengerOfSameVehicle(entity) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }
}
