package doggytalents.entity;

import java.util.UUID;

import com.google.common.base.Strings;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 */
public class LevelUtil {
		
	private EntityDog dog;
	private static UUID HEALTH_BOOST_ID = UUID.fromString("da97255c-6281-45db-8198-f79226438583");
	
    public LevelUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public void writeTalentsToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("levels", this.getSaveString());			
	}

	public void readTalentsFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("levels", 8))
			this.dog.getDataWatcher().updateObject(24, tagCompound.getString("levels"));
		else
			this.dog.getDataWatcher().updateObject(24, "0:0");
	}
	
	public String getSaveString() {
		String saveString = this.dog.getDataWatcher().getWatchableObjectString(24);
		return Strings.isNullOrEmpty(saveString) ? "0:0" : saveString;
	}
	
	public int getLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[0]);
	}
	
	public int getDireLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[1]);
	}
	
	public void increaseLevel() {
		this.setLevel(this.getLevel() + 1);
	}
	
	public void increaseDireLevel() {
		this.setDireLevel(this.getDireLevel() + 1);
	}
	
	public void setLevel(int level) {
		this.dog.getDataWatcher().updateObject(24, level + ":" + this.getDireLevel());
		this.updateHealthModifier();
	}
	
	public void setDireLevel(int level) {
		this.dog.getDataWatcher().updateObject(24, this.getLevel() + ":" + level);
		this.updateHealthModifier();
	}
	
	public void updateHealthModifier() {
		IAttributeInstance iattributeinstance = this.dog.getEntityAttribute(SharedMonsterAttributes.maxHealth);

		AttributeModifier healthModifier = this.createHealthModifier(this.dog.effectiveLevel() + 1.0D);
		
        if(iattributeinstance.getModifier(HEALTH_BOOST_ID) != null)
            iattributeinstance.removeModifier(healthModifier);

        iattributeinstance.applyModifier(healthModifier);
	}
	
	public AttributeModifier createHealthModifier(double health) {
		return new AttributeModifier(HEALTH_BOOST_ID, "Dog Health", health, 0);
	}

	public boolean isDireDog() {
		return this.getDireLevel() == 30;
	}
}
