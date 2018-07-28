package doggytalents.helper;

import java.util.ArrayList;
import java.util.List;

import doggytalents.entity.EntityAbstractDog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class DogLocationManager extends WorldSavedData {
	
	public List<DogLocation> locations;
	
	public DogLocationManager(String name) {
		super(name);
		this.locations = new ArrayList<DogLocation>();
	}
	
	/**
	 * Gets DogLocationHandler for the given world or creates on if it doesn't exist 
	 */
	public static DogLocationManager getHandler(World world) {
		DogLocationManager locationManager = (DogLocationManager)world.getPerWorldStorage().getOrLoadData(DogLocationManager.class, "dog_locations");
		
    	if (locationManager == null) {
            locationManager = new DogLocationManager("dog_locations");
            world.getPerWorldStorage().setData("dog_locations", locationManager);
        }
    	
    	return locationManager;
	}
	
	public void addOrUpdateLocation(EntityAbstractDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		for (int i = 0; i < this.locations.size(); i++) {
			DogLocation loc = this.locations.get(i);
			
			if (loc.equals(temp)) {
				this.locations.set(i, temp);
				this.markDirty();
				return;
			}
		}
		
		this.locations.add(temp);
		this.markDirty();
	}
	
	/**
	 * This should be run whenever a dog is loaded back into a world after being unloaded at some point
	 */
	public void updateEntityId(EntityAbstractDog dog) {
		
		// Matches this dog with it's saved DogLocation
		for (int i = 0; i < this.locations.size(); i++) {
			DogLocation loc = this.locations.get(i);
            if (loc.entityId < 0 && this.axisCoordEqual(dog.posX, loc.x) && this.axisCoordEqual(dog.posY, loc.y) && 
            		this.axisCoordEqual(dog.posZ, loc.z) && dog.world.provider.getDimension() == loc.dim) {
            	
            	loc.entityId = dog.getEntityId();
            	return;
            }
		}
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
		public int entityId = -1;
		
		public DogLocation(NBTTagCompound nbt) {
			this.readFromNBT(nbt);
		}
		
		public DogLocation(EntityAbstractDog dog) {
			this.x = dog.posX;
			this.y = dog.posY;
			this.z = dog.posZ;
			this.dim = dog.world.provider.getDimension();
			this.entityId = dog.getEntityId();
		}

		public void readFromNBT(NBTTagCompound nbt) {
			this.x = nbt.getDouble("x");
			this.y = nbt.getDouble("y");
			this.z = nbt.getDouble("z");
			this.dim = nbt.getInteger("dim");
		}

		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setDouble("x", this.x);
			nbt.setDouble("y", this.y);
			nbt.setDouble("z", this.z);
			nbt.setInteger("dim", this.dim);
			
			return nbt;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DogLocation)) return false;
			
			DogLocation other = (DogLocation)obj;
			
			return this.entityId >= 0 && other.entityId >= 0 && this.entityId == other.entityId;  
		}
		
		@Override
		public int hashCode() {
			return this.entityId;
		}
		
		@Override
		public String toString() {
			return String.format("DogLocation [x=%f,y=%f, z=%f, dim=%d]", this.x, this.y, this.z, this.dim);
		}
	}
}
