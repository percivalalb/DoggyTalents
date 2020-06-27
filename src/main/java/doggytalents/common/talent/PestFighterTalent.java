package doggytalents.common.talent;

import java.util.Iterator;
import java.util.List;

import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;

public class PestFighterTalent extends Talent {


    @Override
    public void livingTick(DogEntity dogIn) {
        if (dogIn.world.isRemote || dogIn.ticksExisted % 2 == 0) {
            return;
        }

        int level = dogIn.getLevel(this);

        if(level >= 0) {
            byte damage = 1;

            if (level >= 5) {
                damage = 2;
            }

            List<SilverfishEntity> list = dogIn.world.getEntitiesWithinAABB(SilverfishEntity.class, dogIn.getBoundingBox().grow(level * 3, 4D, level * 3), EntityPredicates.IS_ALIVE);
            Iterator<SilverfishEntity> iterator = list.iterator();

            while (iterator.hasNext()) {
                SilverfishEntity entitySilverfish = iterator.next();
                if (dogIn.getRNG().nextInt(10) == 0) {
                    entitySilverfish.attackEntityFrom(DamageSource.GENERIC, damage);
                }
            }
        }
    }
}
