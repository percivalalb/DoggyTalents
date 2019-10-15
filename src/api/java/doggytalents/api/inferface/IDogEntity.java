package doggytalents.api.inferface;

import doggytalents.api.feature.IDog;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;

public abstract class IDogEntity extends EntityTameable implements IDog {

    protected IDogEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    public EntityTameable getDog() {
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
