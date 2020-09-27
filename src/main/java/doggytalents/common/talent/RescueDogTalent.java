package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class RescueDogTalent extends Talent {

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.world.isRemote) {
            return;
        }

        int level = dogIn.getLevel(this);

        if (level > 0) {
            LivingEntity owner = dogIn.getOwner();

            //TODO add particles and check how far away dog is
            if (owner != null && owner.getHealth() <= 6) {
                int healCost = this.healCost(level);

                if (dogIn.getDogHunger() >= healCost) {
                    owner.heal(MathHelper.floor(level * 1.5D));
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
