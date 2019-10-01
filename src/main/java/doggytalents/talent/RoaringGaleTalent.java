package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.nbt.CompoundNBT;

public class RoaringGaleTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("roarcooldown", 0);
    }

    @Override
    public void writeAdditional(IDogEntity dog, CompoundNBT tagCompound) {
        int roarCooldown = dog.getObject("roarcooldown", Integer.TYPE);
        tagCompound.putInt("roarcooldown", roarCooldown);
    }

    @Override
    public void readAdditional(IDogEntity dog, CompoundNBT tagCompound) {
        dog.putObject("roarcooldown", tagCompound.getInt("roarcooldown"));
    }

    @Override
    public void livingTick(IDogEntity dog) {
        int roarCooldown = dog.getObject("roarcooldown", Integer.TYPE);

        if(roarCooldown > 0) {
            roarCooldown--;
            dog.putObject("roarcooldown", roarCooldown);
            return;
        }
    }
}
