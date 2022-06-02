package doggytalents.common.talent;

import doggytalents.DoggyAttributes;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BlackPeltTalent extends TalentInstance {

    private static final UUID BLACK_PELT_DAMAGE_ID = UUID.fromString("9abeafa9-3913-4b4c-b46e-0f1548fb19b3");
    private static final UUID BLACK_PELT_CRIT_CHANCE_ID = UUID.fromString("f07b5d39-a8cc-4d32-b458-6efdf1dc6836");
    private static final UUID BLACK_PELT_CRIT_BONUS_ID = UUID.fromString("e19e0d42-6ee3-4ee1-af1c-7519af4354cd");

    public BlackPeltTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDog dogIn) {
        dogIn.setAttributeModifier(Attributes.ATTACK_DAMAGE, BLACK_PELT_DAMAGE_ID, this::createPeltModifier);
        dogIn.setAttributeModifier(DoggyAttributes.CRIT_CHANCE.get(), BLACK_PELT_CRIT_CHANCE_ID, this::createPeltCritChance);
        dogIn.setAttributeModifier(DoggyAttributes.CRIT_BONUS.get(), BLACK_PELT_CRIT_BONUS_ID, this::createPeltCritBonus);
    }

    @Override
    public void set(AbstractDog dogIn, int levelBefore) {
        dogIn.setAttributeModifier(Attributes.ATTACK_DAMAGE, BLACK_PELT_DAMAGE_ID, this::createPeltModifier);
        dogIn.setAttributeModifier(DoggyAttributes.CRIT_CHANCE.get(), BLACK_PELT_CRIT_CHANCE_ID, this::createPeltCritChance);
        dogIn.setAttributeModifier(DoggyAttributes.CRIT_BONUS.get(), BLACK_PELT_CRIT_BONUS_ID, this::createPeltCritBonus);
    }

    public AttributeModifier createPeltModifier(AbstractDog dogIn, UUID uuidIn) {
        if (this.level() > 0) {
            double damageBonus = this.level();

            if (this.level() >= 5) {
                damageBonus += 2;
            }

            return new AttributeModifier(uuidIn, "Black Pelt", damageBonus, AttributeModifier.Operation.ADDITION);
        }

        return null;
    }

    public AttributeModifier createPeltCritChance(AbstractDog dogIn, UUID uuidIn) {
        if (this.level() <= 0) {
            return null;
        }

        double damageBonus = 0.15D * this.level();

        if (this.level() >= 5) {
            damageBonus = 1D;
        }

        return new AttributeModifier(uuidIn, "Black Pelt Crit Chance", damageBonus, AttributeModifier.Operation.ADDITION);
    }

    public AttributeModifier createPeltCritBonus(AbstractDog dogIn, UUID uuidIn) {
        if (this.level() <= 0) {
            return null;
        }

        return new AttributeModifier(uuidIn, "Black Pelt Crit Bonus", 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
