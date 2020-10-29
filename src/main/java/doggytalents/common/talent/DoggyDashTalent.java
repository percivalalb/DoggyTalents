package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

public class DoggyDashTalent extends TalentInstance {

    private static final UUID DASH_BOOST_ID = UUID.fromString("50671e49-1ded-4097-902b-78bb6b178772");

    public DoggyDashTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(Attributes.MOVEMENT_SPEED, DASH_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(Attributes.MOVEMENT_SPEED, DASH_BOOST_ID, this::createSpeedModifier);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        if (this.level() > 0) {
            double speed = 0.03D * this.level();

            if (this.level() >= 5) {
                speed += 0.04D;
            }

            return new AttributeModifier(uuidIn, "Doggy Dash", speed, AttributeModifier.Operation.ADDITION);
        }

        return null;
    }
}
