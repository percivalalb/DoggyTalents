package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class PillowPawTalent extends Talent {

    private static final UUID PILLOW_PAW_BOOST_ID = UUID.fromString("1f002df0-9d35-49c6-a863-b8945caa4af4");

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(LivingEntity.ENTITY_GRAVITY, PILLOW_PAW_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(LivingEntity.ENTITY_GRAVITY, PILLOW_PAW_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void removed(AbstractDogEntity dogIn, int preLevel) {
        dogIn.removeAttributeModifier(LivingEntity.ENTITY_GRAVITY, PILLOW_PAW_BOOST_ID);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        int level = dogIn.getLevel(this);

        if (level >= 5) {
            return new AttributeModifier(uuidIn, "Pillow Paw", -0.065D, AttributeModifier.Operation.ADDITION).setSaved(false);
        }

        return null;
    }

    @Override
    public ActionResultType canTrample(AbstractDogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        int level = dogIn.getLevel(this);
        return level >= 5 ? ActionResultType.FAIL : ActionResultType.PASS;
    }

    @Override
    public ActionResultType onLivingFall(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        int level = dogIn.getLevel(this);
        return level >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResult<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        int level = dogIn.getLevel(this);
        if (level > 0) {
            return ActionResult.resultSuccess(distance - level * 3);
        }

        return ActionResult.resultPass(0F);
    }
}
