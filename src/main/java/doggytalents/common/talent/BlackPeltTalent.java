package doggytalents.common.talent;

import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BlackPeltTalent extends Talent {

    private static final UUID BLACK_PELT_DAMAGE_ID = UUID.fromString("9abeafa9-3913-4b4c-b46e-0f1548fb19b3");

    @Override
    public ActionResultType attackEntityAsMob(AbstractDogEntity dog, Entity entity) {
        int level = dog.getLevel(this);

        //TODO redo crit to be better in line with text info
        if (level == 5 || dog.getRNG().nextInt(6) < level) {
            //damage += (damage + 1) / 2;

            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().particles.addParticleEmitter(entity, ParticleTypes.CRIT));
        }
        return ActionResultType.PASS;
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        int level = dogIn.getLevel(this);
        this.updateAttackDamage(dogIn, this.calculateDamage(level));
    }

    @Override
    public void set(AbstractDogEntity dogIn, int level) {
        this.updateAttackDamage(dogIn, this.calculateDamage(level));
    }

    public double calculateDamage(int level) {
        double damage = level;

        if (level >= 5) {
            damage += 2;
        }

        return damage;
    }

    public void updateAttackDamage(AbstractDogEntity dogIn, double speed) {
        IAttributeInstance damageInstance = dogIn.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        AttributeModifier speedModifier = this.createPeltModifier(speed);

        if(damageInstance.getModifier(BLACK_PELT_DAMAGE_ID) != null) {
            damageInstance.removeModifier(speedModifier);
        }

        damageInstance.applyModifier(speedModifier);
    }

    public AttributeModifier createPeltModifier(double speed) {
        return new AttributeModifier(BLACK_PELT_DAMAGE_ID, "Black Pelt", speed, AttributeModifier.Operation.ADDITION);
    }
}
