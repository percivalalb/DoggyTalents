package doggytalents.entity.features;

import java.util.UUID;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author ProPercivalalb
 */
public class LevelFeature extends DogFeature {
		
	private static UUID HEALTH_BOOST_ID = UUID.fromString("da97255c-6281-45db-8198-f79226438583");
	
    public LevelFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
    @Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("level_normal", this.getLevel());		
		compound.putInt("level_dire", this.getDireLevel());	
	}

    @Override
	public void readAdditional(CompoundNBT compound) {
		if(compound.contains("level_normal"))
			this.setLevel(compound.getInt("level_normal"));
		
		if(compound.contains("level_dire"))
			this.setDireLevel(compound.getInt("level_dire"));
		
		//Backwards compatibility
		if(compound.contains("levels", 8)) {
			String[] split = compound.getString("levels").split(":");
			this.setLevel(new Integer(split[0]));
			this.setDireLevel(new Integer(split[1]));
		}
	}
	
	public int getLevel() {
		return this.dog.getLevel();
	}
	
	public int getDireLevel() {
		return this.dog.getDireLevel();
	}
	
	public void increaseLevel() {
		this.setLevel(this.getLevel() + 1);
	}
	
	public void increaseDireLevel() {
		this.setDireLevel(this.getDireLevel() + 1);
	}
	
	public void setLevel(int level) {
		this.dog.setLevel(level);
		this.updateHealthModifier();
	}
	
	public void setDireLevel(int level) {
		this.dog.setDireLevel(level);
		this.updateHealthModifier();
	}
	
	public void updateHealthModifier() {
		IAttributeInstance iattributeinstance = this.dog.getAttribute(SharedMonsterAttributes.MAX_HEALTH);

		AttributeModifier healthModifier = this.createHealthModifier(this.dog.effectiveLevel() + 1.0D);
		
        if(iattributeinstance.getModifier(HEALTH_BOOST_ID) != null)
            iattributeinstance.removeModifier(healthModifier);

        iattributeinstance.applyModifier(healthModifier);
	}
	
	public AttributeModifier createHealthModifier(double health) {
		return new AttributeModifier(HEALTH_BOOST_ID, "Dog Health", health, AttributeModifier.Operation.ADDITION);
	}

	public boolean isDireDog() {
		return this.getDireLevel() == 30;
	}
}
