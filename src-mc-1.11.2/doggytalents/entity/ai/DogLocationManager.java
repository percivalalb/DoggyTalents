package doggytalents.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSavedDataCallableSave;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

public class DogLocationManager extends WorldSavedData {
	
	private List<DogLocation> locations;
	
	public DogLocationManager(String name) {
		super(name);
		this.locations = new ArrayList<DogLocation>();
	}
	
	public List<DogLocation> getList(int dim, Predicate<DogLocation> selector) {
		return this.getStream(dim, selector).collect(Collectors.toList());
	}
	
	public Stream<DogLocation> getStream(int dim, Predicate<DogLocation> selector) {
		return this.locations.stream().filter(loc -> loc.dim == dim).filter(selector);
	}
	
	public List<DogLocation> getAll(Predicate<DogLocation> selector) {
		return this.locations.stream().filter(selector).collect(Collectors.toList());
	}
	
	/**
	 * Gets DogLocationHandler uses Overworld WorldSaveData as that should always be loaded
	 */
	public static DogLocationManager getHandler(World world) {
		if(world.getMinecraftServer() == null) {
			DoggyTalents.LOGGER.warn("Called DogLocation#getHandler on the client, this shouldn't happen");
			return null;
		}
		
		MapStorage storage = world.getMinecraftServer().getWorld(0).getMapStorage();
		DogLocationManager locationManager = (DogLocationManager)storage.getOrLoadData(DogLocationManager.class, "dog_locations");
		
    	if (locationManager == null) {
            locationManager = new DogLocationManager("dog_locations");
			storage.setData("dog_locations", locationManager);
		}
    	
    	return locationManager;
	}
	
	public void update(EntityDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		int index = this.locations.indexOf(temp);
		if(index >= 0) {
			this.locations.set(index, temp);
		} else {
			DoggyTalents.LOGGER.debug("ADDED NEW DATA: " + temp);
			this.locations.add(temp);
		}

		this.markDirty();
	}

	public void remove(EntityDog dog) {
		DogLocation temp = new DogLocation(dog);
		
		if(this.locations.remove(temp)) {
			this.markDirty();
			DoggyTalents.LOGGER.debug("REMOVED DATA: "+ temp);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("dog_locations", 9)) {
			NBTTagList nbtlist = nbt.getTagList("dog_locations", 10);
		
			for(int i = 0; i < nbtlist.tagCount(); i++) {
				DogLocation location = new DogLocation(nbtlist.getCompoundTagAt(i));
				DoggyTalents.LOGGER.debug("Loaded: " + location);
				if(location.entityId != null)
					this.locations.add(location);
			}
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

		private @Nonnull UUID entityId;
		public double x, y, z;
		public int dim;
		
		// Dog Data
		private @Nullable UUID owner;
		private ITextComponent name;
		private String gender;
		private boolean hasRadarCollar;
		
		public DogLocation(NBTTagCompound nbt) {
			this.readFromNBT(nbt);
		}
		
		public DogLocation(EntityDog dog) {
			this.x = dog.posX;
			this.y = dog.posY;
			this.z = dog.posZ;
			this.dim = dog.world.provider.getDimension();
			this.entityId = dog.getUniqueID();
			this.owner = dog.getOwnerId();
			
			this.name = dog.getDisplayName();
			this.gender = dog.getGender();
			this.hasRadarCollar = dog.hasRadarCollar();
		}

		public void readFromNBT(NBTTagCompound compound) {
			this.x = compound.getDouble("x");
			this.y = compound.getDouble("y");
			this.z = compound.getDouble("z");
			this.dim = compound.getInteger("dim");
			this.entityId = compound.getUniqueId("entityId");
			if(compound.hasUniqueId("ownerId")) this.owner = compound.getUniqueId("ownerId");
			
			if(compound.hasKey("name_text_component", 8)) {
				this.name = ITextComponent.Serializer.jsonToComponent(compound.getString("name_text_component"));
			} else if(compound.hasKey("name", 8)) { //
				this.name = new TextComponentString(compound.getString("name"));
			}
			
			this.gender = compound.getString("gender");
			this.hasRadarCollar = compound.getBoolean("collar");
		}

		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			compound.setDouble("x", this.x);
			compound.setDouble("y", this.y);
			compound.setDouble("z", this.z);
			compound.setInteger("dim", this.dim);
			compound.setUniqueId("entityId", this.entityId);
			
			if(this.owner != null) compound.setUniqueId("ownerId", this.owner);
			
			compound.setString("name_text_component", ITextComponent.Serializer.componentToJson(this.name));
			compound.setString("gender", this.gender);
			compound.setBoolean("collar", this.hasRadarCollar);
			return compound;
		}
		
		public EntityDog getDog(World world) {
			if(!(world instanceof WorldServer)) {
				DoggyTalents.LOGGER.warn("Something when wrong. Tried to call DogLocation#getDog(EntityPlayer) on what looks like the client side");
				return null;
			}
			
			Entity entity = ((WorldServer)world).getEntityFromUuid(this.entityId);
			
			if(entity == null) {
				return null;
			}
			else if(!(entity instanceof EntityDog)) {
				DoggyTalents.LOGGER.warn("Something when wrong. The saved dog UUID is not an EntityDog");
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
			return String.format("DogLocation [x=%f,y=%f, z=%f, dim=%d]", this.x, this.y, this.z, this.dim);
		}
	}
}
