package doggytalents.entity;

import java.util.UUID;

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
		tagCompound.setInteger("level_normal", this.getLevel());		
		tagCompound.setInteger("level_dire", this.getDireLevel());	
	}

	public void readTalentsFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("level_normal"))
			this.setLevel(tagCompound.getInteger("level_normal"));
		
		if(tagCompound.hasKey("level_dire"))
			this.setDireLevel(tagCompound.getInteger("level_dire"));
		
		//Backwards compatibility
		if(tagCompound.hasKey("levels", 8)) {
			String[] split = tagCompound.getString("levels").split(":");
			this.setLevel(new Integer(split[0]));
			this.setDireLevel(new Integer(split[1]));
		}
	}
	
	public int getLevel() {
		return this.dog.getDataWatcher().get(EntityDog.LEVEL);
	}
	
	public int getDireLevel() {
		return this.dog.getDataWatcher().get(EntityDog.LEVEL_DIRE);
	}
	
	public void increaseLevel() {
		this.setLevel(this.getLevel() + 1);
	}
	
	public void increaseDireLevel() {
		this.setDireLevel(this.getDireLevel() + 1);
	}
	
	public void setLevel(int level) {
		this.dog.getDataWatcher().set(EntityDog.LEVEL, level);
		this.updateHealthModifier();
	}
	
	public void setDireLevel(int level) {
		this.dog.getDataWatcher().set(EntityDog.LEVEL_DIRE, level);
		this.updateHealthModifier();
	}
	
	public void updateHealthModifier() {
		IAttributeInstance iattributeinstance = this.dog.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

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
