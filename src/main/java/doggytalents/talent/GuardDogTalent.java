package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 */
public class GuardDogTalent extends Talent {

    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("guardtime", 0);
    }
    
    @Override
    public void livingTick(EntityDog dog) {
        int guardTime = (Integer)dog.objects.get("guardtime");
        if(guardTime > 0) {
            dog.objects.put("guardtime", guardTime - 1);
        }
    }
    
    @Override
    public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
        Entity entity = damageSource.getTrueSource();
        int guardTime = (Integer)dog.objects.get("guardtime");
        
        if (entity != null && guardTime <= 0) {
            int level = dog.TALENTS.getLevel(this);
            int blockChance = level != 5 ? 0 : 1;
            blockChance += level;
            
            if (dog.getRNG().nextInt(12) < blockChance) {
                dog.objects.put("guardtime", 10);
                dog.playSound(SoundEvents.ENTITY_ITEM_BREAK, dog.getSoundVolume(), (dog.getRNG().nextFloat() - dog.getRNG().nextFloat()) * 0.2F + 1.0F);
                
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int onRegenerationTick(EntityDog dog, int totalInTick) { 
        if(dog.TALENTS.getLevel(this) >= 5)
            if(dog.getRNG().nextInt(2) == 0)
                totalInTick += 1;
        return totalInTick; 
    }
}
