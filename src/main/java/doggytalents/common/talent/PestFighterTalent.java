package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Silverfish;

import java.util.List;

public class PestFighterTalent extends TalentInstance {

    public PestFighterTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDog dogIn) {
        if (dogIn.level.isClientSide || dogIn.tickCount % 2 == 0) {
            return;
        }

        if (this.level() >= 0) {
            byte damage = 1;

            if (this.level() >= 5) {
                damage = 2;
            }

            List<Silverfish> list = dogIn.level.getEntitiesOfClass(
                Silverfish.class, dogIn.getBoundingBox().inflate(this.level() * 3, 4D, this.level() * 3), EntitySelector.ENTITY_STILL_ALIVE
            );
            for (Silverfish silverfish : list) {
                if (dogIn.getRandom().nextInt(10) == 0) {
                    silverfish.hurt(DamageSource.GENERIC, damage);
                }
            }
        }
    }
}
