package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeMod;

public class PillowPawTalent extends TalentInstance {

    private static final UUID PILLOW_PAW_BOOST_ID = UUID.fromString("1f002df0-9d35-49c6-a863-b8945caa4af4");

    public PillowPawTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), PILLOW_PAW_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), PILLOW_PAW_BOOST_ID, this::createSpeedModifier);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        if (this.level() >= 5) {
            return new AttributeModifier(uuidIn, "Pillow Paw", -0.065D, AttributeModifier.Operation.ADDITION);
        }

        return null;
    }

    @Override
    public ActionResultType canTrample(AbstractDogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        return this.level() >= 5 ? ActionResultType.FAIL : ActionResultType.PASS;
    }

    @Override
    public ActionResultType onLivingFall(AbstractDogEntity dogIn, float distance, float damageMultiplier) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResult<Float> calculateFallDistance(AbstractDogEntity dogIn, float distance) {
        if (this.level() > 0) {
            return ActionResult.resultSuccess(distance - this.level() * 3);
        }

        return ActionResult.resultPass(0F);
    }
}
