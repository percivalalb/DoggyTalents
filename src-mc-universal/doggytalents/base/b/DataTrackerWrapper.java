package doggytalents.base.b;

import com.google.common.base.Strings;

import doggytalents.base.IDataTracker;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.DataWatcher;
import net.minecraft.util.MathHelper;

public class DataTrackerWrapper implements IDataTracker {

	public EntityDog dog;
	
	public DataTrackerWrapper(EntityDog dogIn) {
		this.dog = dogIn;
	}
		
	public DataWatcher getDataWatcher() {
		return this.dog.getDataWatcher();
	}
	
	@Override
	public void entityInit() {
        this.getDataWatcher().addObject(19, new Integer((-2))); //Dog Cape
		this.getDataWatcher().addObject(20, new Integer((-2))); //Dog Collar
        this.getDataWatcher().addObject(21, new String("")); //Dog Name
        this.getDataWatcher().addObject(22, new String("")); //Talent Data
        this.getDataWatcher().addObject(23, new Integer(60)); //Dog Hunger
        this.getDataWatcher().addObject(24, new String("0:0")); //Level Data
        this.getDataWatcher().addObject(25, new Integer(0)); //Boolean data
        this.getDataWatcher().addObject(26, 0); //Texture index
        this.getDataWatcher().addObject(27, new Integer(0)); //Dog Mode
        this.getDataWatcher().addObject(28, "-1:-1:-1:-1:-1:-1"); //Dog Mode
		
	}
	
	@Override
	public void setBegging(boolean flag) {
		this.setCustomData(5, flag);
	}
	
	@Override
	public boolean isBegging() {
		return this.getCustomData(5);
	}

	@Override
	public void setTameSkin(int value) {
		this.getDataWatcher().updateObject(26, value);
	}

	@Override
	public int getTameSkin() {
		return this.getDataWatcher().getWatchableObjectInt(26);
	}

	@Override
	public void setWillObeyOthers(boolean flag) {
		this.setCustomData(4, flag);
	}

	@Override
	public boolean willObeyOthers() {
		return this.getCustomData(4);
	}

	@Override
	public void setFriendlyFire(boolean flag) {
		this.setCustomData(0, flag);
	}

	@Override
	public boolean canFriendlyFire() {
		return this.getCustomData(0);
	}

	@Override
	public int getDogHunger() {
		return this.getDataWatcher().getWatchableObjectInt(23);
	}

	@Override
    public void setDogHunger(int par1) {
    	this.getDataWatcher().updateObject(23, MathHelper.clamp_int(par1, 0, 120));
    }

	@Override
	public void hasRadarCollar(boolean flag) {
    	this.setCustomData(1, flag);
    }
    
	@Override
    public boolean hasRadarCollar() {
      	return this.getCustomData(1);
    }

	@Override
	public void setHasBone(boolean flag) {
		this.setCustomData(2, flag);
	}

	@Override
	public boolean hasBone() {
		return this.getCustomData(2);
	}

	@Override
	public void setHasSunglasses(boolean flag) {
		this.setCustomData(3, flag);
	}

	@Override
	public boolean hasSunglasses() {
		return this.getCustomData(3);
	}

	@Override
	public void setCollarColour(int value) {
		this.getDataWatcher().updateObject(20, value);
		
	}

	@Override
	public int getCollarColour() {
		return this.getDataWatcher().getWatchableObjectInt(20);
	}

	@Override
	public void setCapeData(int value) {
		this.getDataWatcher().updateObject(19, value);
	}

	@Override
	public int getCapeData() {
		return this.getDataWatcher().getWatchableObjectInt(19);
	}
	
	public void setCustomData(int BIT, boolean flag) {
    	int in = this.getDataWatcher().getWatchableObjectInt(25);
    	if(flag) in |= (1 << BIT);
    	else in &= ~(1 << BIT);
    	this.getDataWatcher().updateObject(25, in);
    }
    
    public boolean getCustomData(int BIT) {
    	return (this.getDataWatcher().getWatchableObjectInt(25) & (1 << BIT)) == (1 << BIT);
    }

	@Override
	public void setLevel(int level) {
		this.getDataWatcher().updateObject(24, level + ":" + this.getDireLevel());
	}

	@Override
	public int getLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[0]);
	}

	@Override
	public void setDireLevel(int level) {
		this.getDataWatcher().updateObject(24, this.getLevel() + ":" + level);
	}

	@Override
	public int getDireLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[1]);
	}
	
	public String getSaveString() {
		String saveString = this.dog.getDataWatcher().getWatchableObjectString(24);
		return Strings.isNullOrEmpty(saveString) ? "0:0" : saveString;
	}

	@Override
	public void setModeId(int mode) {
		this.dog.getDataWatcher().updateObject(27, Math.min(mode, EnumMode.values().length - 1));
	}

	@Override
	public int getModeId() {
		return this.dog.getDataWatcher().getWatchableObjectInt(27);
	}
	
	@Override
	public void setTalentString(String data) {
		this.dog.getDataWatcher().updateObject(22, data);
	}

	@Override
	public String getTalentString() {
		return this.dog.getDataWatcher().getWatchableObjectString(22);
	}

	@Override
	public boolean hasBowlPos() {
		return this.getBowlX() != -1 && this.getBowlY() != -1 && this.getBowlZ() != -1;
	}
	
	@Override
	public boolean hasBedPos() {
		return this.getBedX() != -1 && this.getBedY() != -1 && this.getBedZ() != -1;
	}

	@Override
	public int getBedX() {
		try{return new Integer(this.getSkillsLevels()[0]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	@Override
	public int getBedY() {
		try{return new Integer(this.getSkillsLevels()[1]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	@Override
	public int getBedZ() {
		try{return new Integer(this.getSkillsLevels()[2]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	@Override
	public int getBowlX() {
		try{return new Integer(this.getSkillsLevels()[3]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	@Override
	public int getBowlY() {
		try{return new Integer(this.getSkillsLevels()[4]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	@Override
	public int getBowlZ() {
		try{return new Integer(this.getSkillsLevels()[5]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	private String[] getSkillsLevels() {
		return this.dog.getDataWatcher().getWatchableObjectString(28).split(":");
	}
	
	@Override
	public void resetBedPosition() {
		this.dog.getDataWatcher().updateObject(28, "-1:-1:-1:" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ());
	}
	
	@Override
	public void resetBowlPosition() {
		this.dog.getDataWatcher().updateObject(28, getBedX() + ":" +  getBedY() + ":" + getBedZ() + "-:-1:-1:-1");
	}

	@Override
	public void setBedPos(int x, int y, int z) {
		String structure = "";
		structure = x + ":" + y + ":" + z+ ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	@Override
	public void setBowlPos(int x, int y, int z) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + x+ ":" + y + ":" + z;
		this.dog.getDataWatcher().updateObject(28, structure);
	}
}
