package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

public class DoggyDashTalent extends Talent {

    private static final UUID DASH_BOOST_ID = UUID.fromString("50671e49-1ded-4097-902b-78bb6b178772");

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setAttributeModifier(Attributes.MOVEMENT_SPEED, DASH_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        dogIn.setAttributeModifier(Attributes.MOVEMENT_SPEED, DASH_BOOST_ID, this::createSpeedModifier);
    }

    @Override
    public void removed(AbstractDogEntity dogIn, int preLevel) {
        dogIn.removeAttributeModifier(Attributes.MOVEMENT_SPEED, DASH_BOOST_ID);
    }

    public AttributeModifier createSpeedModifier(AbstractDogEntity dogIn, UUID uuidIn) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            double speed = 0.03D * level;

            if (level >= 5) {
                speed += 0.04D;
            }

            return new AttributeModifier(uuidIn, "Doggy Dash", speed, AttributeModifier.Operation.ADDITION);
        }

        return null;
    }
}
