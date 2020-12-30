package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class CreeperSweeperTalent extends TalentInstance {

    private int cooldown;

    public CreeperSweeperTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        this.cooldown = dogIn.ticksExisted;
    }

    @Override
    public void tick(AbstractDogEntity dogIn) {
        if (this.level() > 0) {
            int timeLeft = this.cooldown - dogIn.ticksExisted;

            if (timeLeft <= 0 && !dogIn.isSitting()) {
                List<CreeperEntity> list = dogIn.world.getEntitiesWithinAABB(CreeperEntity.class, dogIn.getBoundingBox().grow(this.level() * 5,this.level() * 2, this.level() * 5));

                if (!list.isEmpty()) {
                    dogIn.playSound(SoundEvents.ENTITY_WOLF_GROWL, dogIn.getSoundVolume(), (dogIn.getRNG().nextFloat() - dogIn.getRNG().nextFloat()) * 0.2F + 1.0F);
                    this.cooldown = dogIn.ticksExisted + 60 + dogIn.getRNG().nextInt(40);
                }
            }

            if (dogIn.getAttackTarget() instanceof CreeperEntity) {
                CreeperEntity creeper = (CreeperEntity) dogIn.getAttackTarget();
                creeper.setCreeperState(-1);
            }
        }
    }

    @Override
    public ActionResultType canAttack(AbstractDogEntity dog, EntityType<?> entityType) {
        return entityType == EntityType.CREEPER && this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType canAttack(AbstractDogEntity dog, LivingEntity entity) {
        return entity instanceof CreeperEntity && this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType shouldAttackEntity(AbstractDogEntity dog, LivingEntity target, LivingEntity owner) {
        return target instanceof CreeperEntity && this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
     }
}
