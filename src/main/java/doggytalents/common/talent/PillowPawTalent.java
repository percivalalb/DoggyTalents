package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class PillowPawTalent extends Talent {

    private static final UUID PILLOW_PAW_BOOST_ID = UUID.fromString("1f002df0-9d35-49c6-a863-b8945caa4af4");

    @Override
    public void init(DogEntity dogIn) {
        int level = dogIn.getLevel(this);
        this.updateGravity(dogIn, level);
    }

    @Override
    public void set(DogEntity dogIn, int level) {
        this.updateGravity(dogIn, level);
    }

    public void updateGravity(DogEntity dogIn, int level) {
        IAttributeInstance speedInstance = dogIn.getAttribute(LivingEntity.ENTITY_GRAVITY);

        AttributeModifier gravityModifier = new AttributeModifier(PILLOW_PAW_BOOST_ID, "Pillow Paw", -0.82D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        if(speedInstance.getModifier(PILLOW_PAW_BOOST_ID) != null) {
            speedInstance.removeModifier(gravityModifier);
        }

        if (level >= 5) {
            speedInstance.applyModifier(gravityModifier);
        }
    }

    @Override
    public ActionResultType canTrample(DogEntity dogIn, BlockState state, BlockPos pos, float fallDistance) {
        int level = dogIn.getLevel(this);
        return level >= 5 ? ActionResultType.FAIL : ActionResultType.PASS;
    }

    //TODO
//    @Override
//    public boolean isImmuneToFalls(DogEntity dog) {
//        return dog.getLevel(this) >= 5;
//    }
//
//    @Override
//    public ActionResult<Integer> fallProtection(DogEntity dog) {
//        return ActionResult.resultSuccess(dog.getLevel(this) * 3);
//    }
}
