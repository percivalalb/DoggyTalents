package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class DoggyDashTalent extends Talent {

    private static final UUID DASH_BOOST_ID = UUID.fromString("50671e49-1ded-4097-902b-78bb6b178772");

    @Override
    public void init(DogEntity dogIn) {
        int level = dogIn.getLevel(this);
        this.updateSpeed(dogIn, this.calculateSpeed(level));
    }

    @Override
    public void set(DogEntity dogIn, int level) {
        this.updateSpeed(dogIn, this.calculateSpeed(level));
    }

    public double calculateSpeed(int level) {
        double speed = 0.03D * level;

        if (level >= 5) {
            speed += 0.04D;
        }

        return speed;
    }

    public void updateSpeed(DogEntity dogIn, double speed) {
        IAttributeInstance speedInstance = dogIn.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier speedModifier = this.createSpeedModifier(speed);

        if(speedInstance.getModifier(DASH_BOOST_ID) != null) {
            speedInstance.removeModifier(speedModifier);
        }

        speedInstance.applyModifier(speedModifier);
    }

    public AttributeModifier createSpeedModifier(double speed) {
        return new AttributeModifier(DASH_BOOST_ID, "Doggy Dash", speed, AttributeModifier.Operation.ADDITION);
    }
}
