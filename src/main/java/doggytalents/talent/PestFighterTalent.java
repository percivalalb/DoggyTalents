package doggytalents.talent;

import java.util.Iterator;
import java.util.List;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class PestFighterTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        int level = dog.getTalentFeature().getLevel(this);

        if(level >= 0) {
            byte damage = 1;

            if (level == 5)
                damage = 2;

            List<SilverfishEntity> list = dog.world.getEntitiesWithinAABB(SilverfishEntity.class, dog.getBoundingBox().grow(level * 3, 4D, level * 3));
            Iterator<SilverfishEntity> iterator = list.iterator();

            while(iterator.hasNext()) {
                SilverfishEntity entitySilverfish = iterator.next();
                if(dog.getRNG().nextInt(20) == 0)
                    entitySilverfish.attackEntityFrom(DamageSource.GENERIC, damage);
            }
        }
    }
}
