package doggytalents.common.entity.ai.nav;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;

public class DogWaterBoundNavigation extends WaterBoundPathNavigation {

    public DogWaterBoundNavigation(Mob p_26594_, Level p_26595_) {
        super(p_26594_, p_26595_);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.isInLiquid();
    }
    
}
