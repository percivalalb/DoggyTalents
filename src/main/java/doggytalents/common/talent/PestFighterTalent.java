package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;

public class PestFighterTalent extends TalentInstance {

    public PestFighterTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.level.isClientSide || dogIn.tickCount % 2 == 0) {
            return;
        }

        if (this.level() >= 0) {
            byte damage = 1;

            if (this.level() >= 5) {
                damage = 2;
            }

            List<SilverfishEntity> list = dogIn.level.getEntitiesOfClass(
                SilverfishEntity.class, dogIn.getBoundingBox().inflate(this.level() * 3, 4D, this.level() * 3), EntityPredicates.ENTITY_STILL_ALIVE
            );
            for (SilverfishEntity silverfish : list) {
                if (dogIn.getRandom().nextInt(10) == 0) {
                    silverfish.hurt(DamageSource.GENERIC, damage);
                }
            }
        }
    }
}
