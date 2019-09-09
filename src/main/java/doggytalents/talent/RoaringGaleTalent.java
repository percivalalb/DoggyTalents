package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;

public class RoaringGaleTalent extends Talent {
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("roarcooldown", 0);
    }
    
    @Override
    public void writeAdditional(EntityDog dog, CompoundNBT tagCompound) {
        int roarCooldown = (Integer)dog.objects.get("roarcooldown");
        tagCompound.putInt("roarcooldown", roarCooldown);
    }
    
    @Override
    public void readAdditional(EntityDog dog, CompoundNBT tagCompound) {
        dog.objects.put("roarcooldown", tagCompound.getInt("roarcooldown"));
    }
    
    @Override
    public void livingTick(EntityDog dog) {
        int roarCooldown = (Integer)dog.objects.get("roarcooldown");

        if(roarCooldown > 0) {
            roarCooldown--;
            dog.objects.put("roarcooldown", roarCooldown);
            return;
        }
    }
}
