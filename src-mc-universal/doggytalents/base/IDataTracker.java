package doggytalents.base;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;

public interface IDataTracker {
	
	public void entityInit();
	
	public void setBegging(boolean flag);
	public boolean isBegging();
	
    public void setTameSkin(int value);
	public int getTameSkin();
    
    public void setWillObeyOthers(boolean flag);
    public boolean willObeyOthers();
    
    public void setFriendlyFire(boolean flag);
    public boolean canFriendlyFire();
    
    public void setDogHunger(int value);
    public int getDogHunger();
    
    public void hasRadarCollar(boolean flag);
    public boolean hasRadarCollar();
    
    public void setHasBone(boolean flag);
    public boolean hasBone();
    
    public void setHasSunglasses(boolean flag);
    public boolean hasSunglasses();
    
    public void setCollarColour(int value);
    public int getCollarColour();
    
    public void setCapeData(int value);
    public int getCapeData();
    
    public void setLevel(int level);
	public int getLevel();
	
	public void setDireLevel(int level);
	public int getDireLevel();
	
	public void setModeId(int mode);
	public int getModeId();
	
	public void setTalentString(String data);
	public String getTalentString();

	public boolean hasBedPos();
	public boolean hasBowlPos();

	public int getBedX();
	public int getBedY();
	public int getBedZ();
	
	public int getBowlX();
	public int getBowlY();
	public int getBowlZ();

	public void resetBedPosition();
	public void resetBowlPosition();

	public void setBedPos(int x, int y, int z);
	public void setBowlPos(int x, int y, int z) ;
}
