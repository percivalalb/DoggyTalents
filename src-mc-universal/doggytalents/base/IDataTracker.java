package doggytalents.base;

public interface IDataTracker {
	
	public void entityInit();
	
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

    
}
