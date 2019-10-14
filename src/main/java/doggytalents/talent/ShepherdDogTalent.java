package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class ShepherdDogTalent extends Talent {
    
    @Override
    public int onHungerTick(EntityDog dog, int totalInTick) { 
        if(dog.getControllingPassenger() != null && !(dog.getControllingPassenger() instanceof EntityPlayer))
            totalInTick += 5 - dog.TALENTS.getLevel(this);
        return totalInTick;
    }
    
    public int getMaxFollowers(EntityDog dog) {
        int level = dog.TALENTS.getLevel(this);
        switch(level) {
        case 1:
            return 1;
        case 2:
            return 2;
        case 3:
            return 4;
        case 4:
            return 8;
        case 5:
            return 16;
        default:
            return 0;
        }
    }
}
