package doggytalents.talent;

import java.util.List;
import java.util.Random;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;

/**
 * @author ProPercivalalb
 */
public class CreeperSweeperTalent extends Talent {

    private Random rand = new Random();

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("creeper_timer", 0);
    }

    @Override
    public void tick(IDogEntity dog) {
        int level = dog.getTalentFeature().getLevel(this);

        if(dog.getAttackTarget() == null && dog.isTamed() && level > 0) {
            List<EntityCreeper> list = dog.world.getEntitiesWithinAABB(EntityCreeper.class, dog.getEntityBoundingBox().grow(level * 5, level * 2, level * 5));

            if(!list.isEmpty() && !dog.isSitting() && dog.getHealth() > 1) {
                int timer = dog.getObject("creeper_timer", Integer.TYPE);
                dog.putObject("creeper_timer", timer - 1);
            }
        }

        if(dog.getObject("creeper_timer", Integer.TYPE) < 0) {
            dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            dog.putObject("creeper_timer", 60 + this.rand.nextInt(40));
        }

        if(dog.getAttackTarget() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper)dog.getAttackTarget();
            creeper.setCreeperState(-1);
        }
    }

    @Override
    public boolean canAttackClass(IDogEntity dog, Class entityClass) {
        return EntityCreeper.class == entityClass && dog.getTalentFeature().getLevel(this) == 5;
    }

    @Override
    public boolean canAttackEntity(IDogEntity dog, Entity entity) {
        return entity instanceof EntityCreeper && dog.getTalentFeature().getLevel(this) == 5;
    }
}
