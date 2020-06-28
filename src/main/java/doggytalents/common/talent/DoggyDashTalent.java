package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class DoggyDashTalent extends Talent {

    private static final UUID DASH_BOOST_ID = UUID.fromString("50671e49-1ded-4097-902b-78bb6b178772");

    @Override
    public void init(AbstractDogEntity dogIn) {
        int level = dogIn.getLevel(this);
        this.updateSpeed(dogIn, this.calculateSpeed(level));
    }

    @Override
    public void removed(AbstractDogEntity dogIn, int preLevel) {
        IAttributeInstance speedInstance = dogIn.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        AttributeModifier speedModifier = speedInstance.getModifier(DASH_BOOST_ID);

        if (speedModifier != null) {
            speedInstance.removeModifier(speedModifier);
        }
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        this.updateSpeed(dogIn, this.calculateSpeed(level));
    }

    public double calculateSpeed(int level) {
        double speed = 0.03D * level;

        if (level >= 5) {
            speed += 0.04D;
        }

        return speed;
    }

    public void updateSpeed(AbstractDogEntity dogIn, double speed) {
        IAttributeInstance speedInstance = dogIn.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier speedModifier = new AttributeModifier(DASH_BOOST_ID, "Doggy Dash", speed, AttributeModifier.Operation.ADDITION);

        if(speedInstance.getModifier(DASH_BOOST_ID) != null) {
            speedInstance.removeModifier(speedModifier);
        }

        speedInstance.applyModifier(speedModifier);
    }
}
