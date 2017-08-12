package doggytalents.base.b;

import doggytalents.base.IDataTracker;
import doggytalents.entity.EntityDog;
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
		this.getDataWatcher().addObject(20, new Integer((-2))); //Dog Collar
        this.getDataWatcher().addObject(21, new String("")); //Dog Name
        this.getDataWatcher().addObject(22, new String("")); //Talent Data
        this.getDataWatcher().addObject(23, new Integer(60)); //Dog Hunger
        this.getDataWatcher().addObject(24, new String("0:0")); //Level Data
        this.getDataWatcher().addObject(25, new Integer(0)); //Boolean data
        this.getDataWatcher().addObject(26, 0); //Texture index
        this.getDataWatcher().addObject(27, new Integer(0)); //Dog Mode
        this.getDataWatcher().addObject(28, "-1:-1:-1:-1:-1:-1"); //Dog Mode
        this.getDataWatcher().addObject(29, new Integer((-2))); //Dog Cape
		
	}

	@Override
	public void setTameSkin(int value) {
		this.getDataWatcher().updateObject(26, value);
	}

	@Override
	public int getTameSkin() {
		return this.getDataWatcher().getWatchableObjectByte(26);
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
		this.getDataWatcher().updateObject(29, value);
	}

	@Override
	public int getCapeData() {
		return this.getDataWatcher().getWatchableObjectInt(29);
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

}
