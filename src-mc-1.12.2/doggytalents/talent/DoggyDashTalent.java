package doggytalents.talent;

import java.util.UUID;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

/**
 * @author ProPercivalalb
 */
public class DoggyDashTalent extends Talent {

    private static UUID DASH_BOOST_ID = UUID.fromString("50671e49-1ded-4097-902b-78bb6b178772");
    
    @Override
    public void onLevelSet(EntityDog dog, int level) {
        IAttributeInstance iattributeinstance = dog.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier speedModifier = this.createSpeedModifier(0.03D * level + (level == 5 ? 0.04D : 0D));
        
        if(iattributeinstance.getModifier(DASH_BOOST_ID) != null)
            iattributeinstance.removeModifier(speedModifier);

        iattributeinstance.applyModifier(speedModifier);
    }
    
    public AttributeModifier createSpeedModifier(double speed) {
        return new AttributeModifier(DASH_BOOST_ID, "Doggy Dash", speed, 0);
    }
}
