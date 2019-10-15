package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class HellHoundTalent extends Talent {

    @Override
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) {
        int level = dog.getTalentFeature().getLevel(this);
        if(level != 0)
            entity.setFire(level);
        return damage;
    }

    @Override
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) {
        if(dog.getTalentFeature().getLevel(this) == 5)
            if(damageSource.isFireDamage())
                return false;

        return true;
    }

    @Override
    public boolean setFire(IDogEntity dog, int amount) {
        return dog.getTalentFeature().getLevel(this) != 5;
    }
}
