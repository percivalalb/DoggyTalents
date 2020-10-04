package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class CreeperSweeperTalent extends Talent {

    private static DataKey<Integer> COOLDOWN = DataKey.make();

    @Override
    public void init(AbstractDogEntity dogIn) {
        dogIn.setDataIfEmpty(COOLDOWN, dogIn.ticksExisted);
    }

    @Override
    public void tick(AbstractDogEntity dogIn) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            int timeLeft = dogIn.getDataOrDefault(COOLDOWN, dogIn.ticksExisted) - dogIn.ticksExisted;

            if (timeLeft <= 0 && !dogIn.isSitting()) {
                List<CreeperEntity> list = dogIn.world.getEntitiesWithinAABB(CreeperEntity.class, dogIn.getBoundingBox().grow(level * 5, level * 2, level * 5));

                if (!list.isEmpty()) {
                    dogIn.playSound(SoundEvents.ENTITY_WOLF_GROWL, dogIn.getSoundVolume(), (dogIn.getRNG().nextFloat() - dogIn.getRNG().nextFloat()) * 0.2F + 1.0F);
                    dogIn.setData(COOLDOWN, dogIn.ticksExisted + 60 + dogIn.getRNG().nextInt(40));
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
        return entityType == EntityType.CREEPER && dog.getLevel(this) >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType canAttack(AbstractDogEntity dog, LivingEntity entity) {
        return entity instanceof CreeperEntity && dog.getLevel(this) >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType shouldAttackEntity(AbstractDogEntity dog, LivingEntity target, LivingEntity owner) {
        return target instanceof CreeperEntity && dog.getLevel(this) >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
     }
}
