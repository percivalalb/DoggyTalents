package doggytalents.talent;

import java.util.List;
import java.util.Random;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.SoundEvents;

/**
 * @author ProPercivalalb
 */
public class CreeperSweeperTalent extends Talent {

    private Random rand = new Random();
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("creeper_timer", 0);
    }

    @Override
    public void tick(EntityDog dog) {
        int level = dog.TALENTS.getLevel(this);
        
        if(dog.getAttackTarget() == null && dog.isTamed() && level > 0) {
            List<CreeperEntity> list = dog.world.getEntitiesWithinAABB(CreeperEntity.class, dog.getBoundingBox().grow(level * 5, level * 2, level * 5));

            if(!list.isEmpty() && !dog.isSitting() && dog.getHealth() > 1) {
                int timer = (int)dog.objects.get("creeper_timer");
                dog.objects.put("creeper_timer", timer - 1);
            }
        }
        
        if((int)dog.objects.get("creeper_timer") < 0) {
            dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            dog.objects.put("creeper_timer", 60 + this.rand.nextInt(40));
        }
        
        
        
        if(dog.getAttackTarget() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity)dog.getAttackTarget();
            creeper.setCreeperState(-1);
        }
    }
    
    @Override
    public boolean canAttack(EntityDog dog, EntityType<?> entityType) {
        return entityType == EntityType.CREEPER && dog.TALENTS.getLevel(this) == 5; 
    }
    
    @Override
    public boolean canAttackEntity(EntityDog dog, Entity entity) {
        return entity instanceof CreeperEntity && dog.TALENTS.getLevel(this) == 5;
    }
}
