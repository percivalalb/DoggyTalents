package doggytalents.talent;

import doggytalents.DoggyTalents;
import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;

/**
 * @author ProPercivalalb
 */
public class BlackPeltTalent extends Talent {

    @Override
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) {
        int level = dog.getTalentFeature().getLevel(this);

        int critChance = level == 5 ? 1 : 0;
        critChance += level;
        //TODO redo crit to be better in line with text info
        if (dog.getRNG().nextInt(6) < critChance) {
            damage += (damage + 1) / 2;
            DoggyTalents.PROXY.spawnCrit(dog.world, entity);
        }
        return damage;
    }
}
