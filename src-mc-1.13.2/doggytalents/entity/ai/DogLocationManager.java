package doggytalents.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataStorage;

public class DogLocationManager extends WorldSavedData {
	
	private List<DogLocation> locations;
	
	public DogLocationManager(String name) {
		super(name);
		this.locations = new ArrayList<DogLocation>();
	}
	
	public List<DogLocation> getList(Predicate<DogLocation> selector) {
		return this.getStream(selector).collect(Collectors.toList());
	}
	
	public Stream<DogLocation> getStream(Predicate<DogLocation> selector) {
		return this.locations.stream().filter(selector);
	}
	
	public static DogLocationManager getHandler(World world) {
		return getHandler(world.getSavedDataStorage(), world.getDimension().getType());
	}
	
	public static DogLocationManager getHandler(World world, DimensionType dimType) {
		return getHandler(world.getSavedDataStorage(), dimType);
	}
	
	public static DogLocationManager getHandler(WorldSavedDataStorage storage, DimensionType dimType) {
		DogLocationManager locationManager = (DogLocationManager)storage.get(dimType, DogLocationManager::new, "dog_locations");
		
    	if(locationManager == null) {
            locationManager = new DogLocationManager("dog_locations");
			storage.set(dimType, "dog_locations", locationManager);
		}
    	
    	return locationManager;
	}
	
	public void update(EntityDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		int index = this.locations.indexOf(temp);
		if(index >= 0) {
			this.locations.set(index, temp);
		} else {
			DoggyTalentsMod.LOGGER.debug("ADDED NEW DATA: " + temp);
			this.locations.add(temp);
		}

		this.markDirty();
	}

	public void remove(EntityDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		if(this.locations.remove(temp)) {
			this.markDirty();
			DoggyTalentsMod.LOGGER.debug("REMOVED DATA: "+ temp);
		}
	}

	@Override
	public void read(NBTTagCompound nbt) {
		if(nbt.contains("dog_locations", 9)) {
			NBTTagList nbtlist = nbt.getList("dog_locations", 10);
		
			for(int i = 0; i < nbtlist.size(); i++) {
				DogLocation location = new DogLocation(nbtlist.getCompound(i));
				DoggyTalentsMod.LOGGER.debug("Loaded: " + location);
				if(location.entityId != null)
					this.locations.add(location);
			}
		}
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		NBTTagList nbtlist = new NBTTagList();
		
		for(DogLocation location : this.locations) {
			nbtlist.add(location.write(new NBTTagCompound()));
		}
		
		compound.put("dog_locations", nbtlist);
		
		return compound;
	}
	
	public class DogLocation {

		private @Nonnull UUID entityId;
		public double x, y, z;
		
		// Dog Data
		private @Nullable UUID owner;
		private ITextComponent name;
		private String gender;
		private boolean hasRadarCollar;
		
		public DogLocation(NBTTagCompound nbt) {
			this.read(nbt);
		}
		
		public DogLocation(EntityDog dog) {
			this.x = dog.posX;
			this.y = dog.posY;
			this.z = dog.posZ;
			this.entityId = dog.getUniqueID();
			this.owner = dog.getOwnerId();
			
			this.name = dog.getName();
			this.gender = dog.getGender();
			this.hasRadarCollar = dog.hasRadarCollar();
		}

		private void read(NBTTagCompound compound) {
			this.x = compound.getDouble("x");
			this.y = compound.getDouble("y");
			this.z = compound.getDouble("z");
			this.entityId = compound.getUniqueId("entityId");
			if(compound.hasUniqueId("ownerId")) this.owner = compound.getUniqueId("ownerId");
			
			if(compound.contains("name_text_component", 8)) {
				this.name = ITextComponent.Serializer.fromJson(compound.getString("name_text_component"));
			} else if(compound.contains("name", 8)) { //
				this.name = new TextComponentString(compound.getString("name"));
			}
			
			this.gender = compound.getString("gender");
			this.hasRadarCollar = compound.getBoolean("collar");
		}

		public NBTTagCompound write(NBTTagCompound compound) {
			compound.putDouble("x", this.x);
			compound.putDouble("y", this.y);
			compound.putDouble("z", this.z);
			compound.putUniqueId("entityId", this.entityId);
			
			if(this.owner != null) compound.putUniqueId("ownerId", this.owner);
			
			compound.putString("name_text_component", ITextComponent.Serializer.toJson(this.name));
			compound.putString("gender", this.gender);
			compound.putBoolean("collar", this.hasRadarCollar);
			return compound;
		}
		
		
		public EntityDog getDog(World world) {			
			if(!(world instanceof WorldServer)) {
				DoggyTalentsMod.LOGGER.warn("Something when wrong. Tried to call DogLocation#getDog(EntityPlayer) on what looks like the client side");
				return null;
			}
			
			Entity entity = ((WorldServer)world).getEntityFromUuid(this.entityId);
			
			if(entity == null) {
				return null;
			}
			else if(!(entity instanceof EntityDog)) {
				DoggyTalentsMod.LOGGER.warn("Something when wrong. The saved dog UUID is not an EntityDog");
				return null;
			}
			
			return (EntityDog)entity;
		}
		
		public EntityLivingBase getOwner(World world) {
			EntityDog dog = this.getDog(world);
			if(dog != null) {
				return dog.getOwner();
			} else if(this.owner != null) {
				return world.getPlayerEntityByUUID(this.owner);
			} else {
				return null;
			}
		}
		
		public ITextComponent getName(World world) {
			EntityDog dog = this.getDog(world);
			if(dog != null) {
				return dog.getDisplayName();
			} else if(this.owner != null) {
				return this.name;
			} else {
				return null;
			}
		}
		
		public boolean hasRadioCollar(World world) {
			EntityDog dog = this.getDog(world);
			if(dog != null) {
				return dog.hasRadarCollar();
			} else {
				return this.hasRadarCollar;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DogLocation)) return false;
			
			DogLocation other = (DogLocation)obj;
			
			return this.entityId != null && other.entityId != null && this.entityId.equals(other.entityId);
		}
		
		/*@Override
		public int hashCode() {
			return this.entityId;
		}*/
		
		@Override
		public String toString() {
			return String.format("DogLocation [id=%s, x=%f,y=%f, z=%f]", this.entityId.toString(), this.x, this.y, this.z);
		}
	}
}
