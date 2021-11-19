package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class RescueDogTalent extends TalentInstance {

    private static final int DISTANCE_TO_HEAL_SQR = 16;

    public RescueDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.level.isClientSide) {
            return;
        }

        if (this.level() > 0) {
            LivingEntity owner = dogIn.getOwner();
            if (owner == null) return;
            float h = owner.getHealth();

            if (
                h <= 6 && h > 0
                && dogIn.distanceToSqr(owner) <= DISTANCE_TO_HEAL_SQR && dogIn.hasLineOfSight(owner)
            ) {
                int healCost = this.healCost(this.level());

                if (dogIn.getDogHunger() >= healCost) {
                    owner.heal(Mth.floor(this.level() * 1.5D));
                    if (dogIn.level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HEART, owner.getX(), owner.getY(), owner.getZ(), this.level*8, owner.getBbWidth(), 0.8f, owner.getBbWidth(), 0.1);
                    }
                    dogIn.setDogHunger(dogIn.getDogHunger() - healCost);
                }
            }
        }
    }

    public int healCost(int level) {
        byte cost = 100;

        if (level >= 5) {
            cost = 80;
        }

        return cost;
    }

    @Override
    public boolean hasRenderer() {
        return true;
    }
}
