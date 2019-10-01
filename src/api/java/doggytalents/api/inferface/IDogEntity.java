package doggytalents.api.inferface;

import doggytalents.api.feature.IDog;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public abstract class IDogEntity extends TameableEntity implements IDog {

    protected IDogEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public TameableEntity getDog() {
        return this;
    }

    // Makes the method public
    @Override
    public float getSoundVolume() {
        return super.getSoundVolume();
    }

    @Override
    public void playTameEffect(boolean play) {
        super.playTameEffect(play);
    }
}
