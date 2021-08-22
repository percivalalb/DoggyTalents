package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundEvents;

public class CreeperSweeperTalent extends TalentInstance {

    private int cooldown;

    public CreeperSweeperTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        this.cooldown = dogIn.tickCount;
    }

    @Override
    public void tick(AbstractDogEntity dogIn) {
        if (this.level() > 0) {
            int timeLeft = this.cooldown - dogIn.tickCount;

            if (timeLeft <= 0 && !dogIn.isInSittingPose()) {
                List<Creeper> list = dogIn.level.getEntitiesOfClass(Creeper.class, dogIn.getBoundingBox().inflate(this.level() * 5,this.level() * 2, this.level() * 5));

                if (!list.isEmpty()) {
                    dogIn.playSound(SoundEvents.WOLF_GROWL, dogIn.getSoundVolume(), (dogIn.getRandom().nextFloat() - dogIn.getRandom().nextFloat()) * 0.2F + 1.0F);
                    this.cooldown = dogIn.tickCount + 60 + dogIn.getRandom().nextInt(40);
                }
            }

            if (dogIn.getTarget() instanceof Creeper) {
                Creeper creeper = (Creeper) dogIn.getTarget();
                creeper.setSwellDir(-1);
            }
        }
    }

    @Override
    public InteractionResult canAttack(AbstractDogEntity dog, EntityType<?> entityType) {
        return entityType == EntityType.CREEPER && this.level() >= 5 ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public InteractionResult canAttack(AbstractDogEntity dog, LivingEntity entity) {
        return entity instanceof Creeper && this.level() >= 5 ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public InteractionResult shouldAttackEntity(AbstractDogEntity dog, LivingEntity target, LivingEntity owner) {
        return target instanceof Creeper && this.level() >= 5 ? InteractionResult.SUCCESS : InteractionResult.PASS;
     }
}
