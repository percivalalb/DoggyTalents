package doggytalents.entity.features;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.feature.IModeFeature;
import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author ProPercivalalb
 **/
public class ModeFeature extends DogFeature implements IModeFeature {

    public ModeFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public EnumMode getMode() {
        return this.dog.getMode();
    }

    @Override
    public void setMode(EnumMode mode) {
        this.dog.setMode(mode);
    }

    @Override
    public boolean isMode(EnumMode mode) {
        return this.getMode() == mode;
    }

    @Override
    public void writeAdditional(CompoundNBT tagCompound) {
        tagCompound.putInt("mode", this.getMode().getIndex());
    }

    @Override
    public void readAdditional(CompoundNBT tagCompound) {
        this.setMode(EnumMode.byIndex(tagCompound.getInt("mode")));
    }
}
