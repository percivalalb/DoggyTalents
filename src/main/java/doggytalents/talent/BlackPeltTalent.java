package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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

            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().particles.addParticleEmitter(entity, ParticleTypes.CRIT));
        }
        return damage;
    }
}
