package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.nbt.NBTTagCompound;

public class RoaringGaleTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("roarcooldown", 0);
    }

    @Override
    public void writeAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        int roarCooldown = dog.getObject("roarcooldown", Integer.TYPE);
        tagCompound.setInteger("roarcooldown", roarCooldown);
    }

    @Override
    public void readAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        dog.putObject("roarcooldown", tagCompound.getInteger("roarcooldown"));
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
