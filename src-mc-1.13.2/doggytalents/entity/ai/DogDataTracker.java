package doggytalents.entity.ai;

import java.util.Optional;

import doggytalents.api.inferface.IDataTracker;
import doggytalents.entity.EntityAbstractDog;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.ModeFeature.EnumMode;
import doggytalents.lib.Constants;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;

public class DogDataTracker implements IDataTracker {

	public static final DataParameter<Boolean> BEGGING = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Byte> DOG_TEXTURE = EntityDataManager.<Byte>createKey(EntityDog.class, DataSerializers.BYTE);
	public static final DataParameter<Integer> COLLAR_COLOUR = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> LEVEL_DIRE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> MODE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<String> TALENTS = EntityDataManager.<String>createKey(EntityDog.class, DataSerializers.STRING);
	public static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> BONE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> FRIENDLY_FIRE = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> OBEY_OTHERS = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> CAPE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> SUNGLASSES = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> RADAR_COLLAR = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Optional<BlockPos>> BOWL_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	public static final DataParameter<Optional<BlockPos>> BED_POS = EntityDataManager.createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	public static final DataParameter<Integer> SIZE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<String> GENDER = EntityDataManager.<String>createKey(EntityDog.class, DataSerializers.STRING);
	
	public EntityDog dog;
	
	public DogDataTracker(EntityDog dogIn) {
		this.dog = dogIn;
	}
		
	public EntityDataManager getDataManager() {
		return this.dog.getDataManager();
	}
	
	@Override
	public void setBegging(boolean flag) {
		this.getDataManager().set(BEGGING, flag);
	}
	
	@Override
	public boolean isBegging() {
		return this.getDataManager().get(BEGGING);
	}
	
	@Override
	public void entityInit() {
        this.getDataManager().register(BEGGING, Boolean.valueOf(false));
		this.getDataManager().register(DOG_TEXTURE, (byte)0);
        this.getDataManager().register(COLLAR_COLOUR, -2);
        this.getDataManager().register(TALENTS, "");
        this.getDataManager().register(HUNGER, Integer.valueOf(60));
        this.getDataManager().register(OBEY_OTHERS, Boolean.valueOf(false));
        this.getDataManager().register(FRIENDLY_FIRE, Boolean.valueOf(false));
        this.getDataManager().register(BONE, -1);
        this.getDataManager().register(RADAR_COLLAR, Boolean.valueOf(false));
        this.getDataManager().register(MODE, Integer.valueOf(0));
        this.getDataManager().register(LEVEL, Integer.valueOf(0));
        this.getDataManager().register(LEVEL_DIRE, Integer.valueOf(0));
        this.getDataManager().register(BOWL_POS, Optional.empty());
        this.getDataManager().register(BED_POS, Optional.empty());
        this.getDataManager().register(CAPE, -2);
        this.getDataManager().register(SUNGLASSES, false);
        this.getDataManager().register(SIZE, Integer.valueOf(3));
        this.getDataManager().register(GENDER, "");
	}
	
	@Override
	public int getTameSkin() {
   	 	return this.getDataManager().get(DOG_TEXTURE);
    }

	@Override
    public void setTameSkin(int index) {
   		this.getDataManager().set(DOG_TEXTURE, (byte)index);
    }
    
	@Override
    public void setWillObeyOthers(boolean flag) {
    	this.getDataManager().set(OBEY_OTHERS, flag);
    }
    
	@Override
    public boolean willObeyOthers() {
    	return this.getDataManager().get(OBEY_OTHERS);
    }
    
	@Override
    public void setFriendlyFire(boolean flag) {
    	this.getDataManager().set(FRIENDLY_FIRE, flag);
    }
    
	@Override
    public boolean canFriendlyFire() {
    	return this.getDataManager().get(FRIENDLY_FIRE);
    }
    
	@Override
    public int getDogHunger() {
		return ((Integer)this.getDataManager().get(HUNGER)).intValue();
	}
    
    @Override
    public void setDogHunger(int par1) {
    	this.getDataManager().set(HUNGER, Math.min(Constants.HUNGER_POINTS, Math.max(0, par1)));
    }
    
    @Override
    public void hasRadarCollar(boolean flag) {
    	this.getDataManager().set(RADAR_COLLAR, Boolean.valueOf(flag));
    }
    
    @Override
    public boolean hasRadarCollar() {
    	return ((Boolean)this.getDataManager().get(RADAR_COLLAR)).booleanValue();
    }
    
    @Override
    public void setNoFetchItem() {
    	this.getDataManager().set(BONE, -1);
    }
    
    @Override    
	public void setBoneVariant(int value) {
    	this.getDataManager().set(BONE, value);
	}
    
    @Override
    public int getBoneVariant() {
    	return this.getDataManager().get(BONE);
    }
    
    @Override
    public boolean hasBone() {
    	return this.getBoneVariant() >= 0;
    }
    
    @Override
    public void setHasSunglasses(boolean hasSunglasses) {
    	this.getDataManager().set(SUNGLASSES, hasSunglasses);
    }
    
    @Override
    public boolean hasSunglasses() {
    	return ((Boolean)this.getDataManager().get(SUNGLASSES)).booleanValue();
    }
    
    @Override
    public int getCollarColour() {
    	return this.getDataManager().get(COLLAR_COLOUR);
    }
    
    @Override
    public void setCollarColour(int value) {
    	this.getDataManager().set(COLLAR_COLOUR, value);
    }
    
    @Override
    public int getCapeData() {
    	return this.getDataManager().get(CAPE);
    }
    
    @Override
    public void setCapeData(int value) {
    	this.getDataManager().set(CAPE, value);
    }
    
    @Override
	public void setDogSize(int value) {
    	this.getDataManager().set(SIZE, Math.min(5, Math.max(1, value)));
    }
    
    @Override
	public int getDogSize() {
    	return this.getDataManager().get(SIZE);
    }
    
    @Override
	public void setGender(String data) {
		this.getDataManager().set(GENDER, data);
	}

	@Override
	public String getGender() {
		return this.getDataManager().get(GENDER);
	}
    
    @Override
	public void setLevel(int level) {
    	this.getDataManager().set(LEVEL, level);	
	}

	@Override
	public int getLevel() {
		return this.getDataManager().get(LEVEL);
	}

	@Override
	public void setDireLevel(int level) {
		this.getDataManager().set(LEVEL_DIRE, level);
	}

	@Override
	public int getDireLevel() {
		return this.getDataManager().get(LEVEL_DIRE);
	}

	@Override
	public void setModeId(int mode) {
		this.getDataManager().set(MODE, Math.min(mode, EnumMode.values().length - 1));
	}

	@Override
	public int getModeId() {
		return this.getDataManager().get(MODE);
	}

	@Override
	public void setTalentString(String data) {
		this.getDataManager().set(TALENTS, data);
	}

	@Override
	public String getTalentString() {
		return this.getDataManager().get(TALENTS);
	}

	@Override
	public boolean hasBedPos() {
		return this.getDataManager().get(BED_POS).isPresent();
	}

	@Override
	public boolean hasBowlPos() {
		return this.getDataManager().get(BOWL_POS).isPresent();
	}

	public BlockPos getBedPos() {
		return this.getDataManager().get(BED_POS).orElse(this.dog.world.getSpawnPoint());
	}
	
	public BlockPos getBowlPos() {
		return this.getDataManager().get(BOWL_POS).orElse(this.dog.getPosition());
	}
	
	@Override
	public int getBedX() {
		return this.getBedPos().getX();
	}

	@Override
	public int getBedY() {
		return this.getBedPos().getY();
	}

	@Override
	public int getBedZ() {
		return this.getBedPos().getZ();
	}

	@Override
	public int getBowlX() {
		return this.getBowlPos().getX();
	}

	@Override
	public int getBowlY() {
		return this.getBowlPos().getY();
	}

	@Override
	public int getBowlZ() {
		return this.getBowlPos().getZ();
	}

	@Override
	public void resetBedPosition() {
		this.getDataManager().set(BED_POS, Optional.empty());
	}

	@Override
	public void resetBowlPosition() {
		this.getDataManager().set(BOWL_POS, Optional.empty());
	}

	@Override
	public void setBedPos(int x, int y, int z) {
		this.getDataManager().set(BED_POS, Optional.of(new BlockPos(x, y, z)));
	}


	@Override
	public void setBowlPos(int x, int y, int z) {
		this.getDataManager().set(BOWL_POS, Optional.of(new BlockPos(x, y, z)));
	}
}
