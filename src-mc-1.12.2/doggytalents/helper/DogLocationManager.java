package doggytalents.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

public class DogLocationManager extends WorldSavedData {
	
	public List<DogLocation> locations;
	
	public DogLocationManager(String name) {
		super(name);
		this.locations = new ArrayList<DogLocation>();
	}
	
	/**
	 * Gets DogLocationHandler uses Overworld WorldSaveData as that should always be loaded
	 */
	public static DogLocationManager getHandler(World world) {
		MapStorage storage = world.getMapStorage();
		DogLocationManager locationManager = (DogLocationManager)storage.getOrLoadData(DogLocationManager.class, "dog_locations");
		
    	if (locationManager == null) {
            locationManager = new DogLocationManager("dog_locations");
			storage.setData("dog_locations", locationManager);
			if(Constants.DEBUG_MODE == true) System.out.println("DATA DECLARED: "+storage.getOrLoadData(locationManager.getClass(), "dog_locations"));
		}
    	
    	return locationManager;
	}
	
	public void addOrUpdateLocation(EntityDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		for (int i = 0; i < this.locations.size(); i++) {
			DogLocation loc = this.locations.get(i);
			
			if (loc.equals(temp)) {
				this.locations.set(i, temp);
				this.markDirty();
				return;
			}
		}
		if(Constants.DEBUG_MODE == true) System.out.println("ADDED NEW DATA: " + temp);
		this.locations.add(temp);
		this.markDirty();
	}

	public void removeDog(EntityDog dog) {
		if(Constants.DEBUG_MODE == true) System.out.println("REMOVED DATA: "+ dog);

		DogLocation temp = new DogLocation(dog);
		this.locations.remove(temp);
		this.markDirty();
	}

	public boolean axisCoordEqual(double a, double b) {
		return Math.abs(a - b) < 1e-4;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList nbtlist = nbt.getTagList("dog_locations", 10);
		
		for (int i = 0; i < nbtlist.tagCount(); i++) {
			this.locations.add(new DogLocation((NBTTagCompound)nbtlist.get(i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound base = new NBTTagCompound();
		
		NBTTagList nbtlist = new NBTTagList();
		
		for (DogLocation location : this.locations) {
			nbtlist.appendTag(location.writeToNBT(new NBTTagCompound()));
		}
		
		base.setTag("dog_locations", nbtlist);
		
		return base;
	}
	
	public class DogLocation {

		public double x, y, z;
		public int dim;
		public UUID entityId;

		// Dog Data
		public String name;
		public String gender;
		public boolean hasRadarCollar;
		
		public DogLocation(NBTTagCompound nbt) {
			this.readFromNBT(nbt);
		}
		
		public DogLocation(EntityDog dog) {
			this.x = dog.posX;
			this.y = dog.posY;
			this.z = dog.posZ;
			this.dim = dog.world.provider.getDimension();
			this.entityId = dog.getUniqueID();
			
			this.name = dog.getName();
			this.gender = dog.getGender();
			this.hasRadarCollar = dog.hasRadarCollar();
		}

		public void readFromNBT(NBTTagCompound nbt) {
			this.x = nbt.getDouble("x");
			this.y = nbt.getDouble("y");
			this.z = nbt.getDouble("z");
			this.dim = nbt.getInteger("dim");
			this.entityId = nbt.getUniqueId("entityId");
			
			this.name = nbt.getString("name");
			this.gender = nbt.getString("gender");
			this.hasRadarCollar = nbt.getBoolean("collar");
		}

		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setDouble("x", this.x);
			nbt.setDouble("y", this.y);
			nbt.setDouble("z", this.z);
			nbt.setInteger("dim", this.dim);
			nbt.setUniqueId("entityId", this.entityId);
			
			nbt.setString("name", this.name);
			nbt.setString("gender", this.gender);
			nbt.setBoolean("collar", this.hasRadarCollar);
			return nbt;
		}
		
		public EntityDog getDog() {
			if(this.entityId == null)
				return null;
			
			World world = DimensionManager.getWorld(this.dim);
			Entity entity = ((WorldServer)world).getEntityFromUuid(this.entityId);
			
			if(entity instanceof EntityDog) 
				return (EntityDog)entity;
			
			return null;
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
			return String.format("DogLocation [x=%f,y=%f, z=%f, dim=%d]", this.x, this.y, this.z, this.dim);
		}
	}
}
