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
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class DogLocationManager extends WorldSavedData {
	
	private List<DogLocation> locations;
	
	public DogLocationManager() {
		super("dog_locations");
		this.locations = new ArrayList<DogLocation>();
	}
	
	public List<DogLocation> getList(DimensionType type, Predicate<DogLocation> selector) {
		return this.getStream(type, selector).collect(Collectors.toList());
	}
	
	public Stream<DogLocation> getStream(DimensionType type, Predicate<DogLocation> selector) {
		return this.locations.stream().filter(loc -> loc.type == type).filter(selector);
	}
	
	
	public static DogLocationManager getHandler(@Nonnull ServerWorld world) {
		return getHandler(world.getServer().getWorld(DimensionType.OVERWORLD).func_217481_x());
	}
	
	public static DogLocationManager getHandler(DimensionSavedDataManager storage) {
		return storage.func_215752_a(DogLocationManager::new, "dog_locations");
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
	public void read(CompoundNBT nbt) {
		if(nbt.contains("dog_locations", 9)) {
			ListNBT nbtlist = nbt.getList("dog_locations", 10);
		
			for(int i = 0; i < nbtlist.size(); i++) {
				DogLocation location = new DogLocation(nbtlist.getCompound(i));
				DoggyTalentsMod.LOGGER.debug("Loaded: " + location);
				if(location.entityId != null)
					this.locations.add(location);
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		ListNBT nbtlist = new ListNBT();
		
		for(DogLocation location : this.locations) {
			nbtlist.add(location.write(new CompoundNBT()));
		}
		
		compound.put("dog_locations", nbtlist);
		
		return compound;
	}
	
	public class DogLocation extends Vec3d {

		private DimensionType type;
		private @Nonnull UUID entityId;
		
		// Dog Data
		private @Nullable UUID owner;
		private ITextComponent name;
		private String gender;
		private boolean hasRadarCollar;
		
		public DogLocation(CompoundNBT compound) {
			super(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
			if(compound.contains("dimension", 8))
				this.type = Registry.field_212622_k.getOrDefault(new ResourceLocation(compound.getString("dimension")));
			this.entityId = compound.getUniqueId("entityId");
			if(compound.hasUniqueId("ownerId")) this.owner = compound.getUniqueId("ownerId");
			
			if(compound.contains("name_text_component", 8)) {
				this.name = ITextComponent.Serializer.fromJson(compound.getString("name_text_component"));
			} else if(compound.contains("name", 8)) { //
				this.name = new StringTextComponent(compound.getString("name"));
			}
			
			this.gender = compound.getString("gender");
			this.hasRadarCollar = compound.getBoolean("collar");
		}
		
		public DogLocation(EntityDog dog) {
			super(dog.posX, dog.posY, dog.posZ);
			this.type = dog.dimension;
			this.entityId = dog.getUniqueID();
			this.owner = dog.getOwnerId();
			
			this.name = dog.getName();
			this.gender = dog.getGender();
			this.hasRadarCollar = dog.hasRadarCollar();
		}

		public CompoundNBT write(CompoundNBT compound) {
			compound.putDouble("x", this.x);
			compound.putDouble("y", this.y);
			compound.putDouble("z", this.z);
			if(this.type != null) compound.putString("dimension", Registry.field_212622_k.getKey(this.type).toString());
			compound.putUniqueId("entityId", this.entityId);
			
			if(this.owner != null) compound.putUniqueId("ownerId", this.owner);
			
			compound.putString("name_text_component", ITextComponent.Serializer.toJson(this.name));
			compound.putString("gender", this.gender);
			compound.putBoolean("collar", this.hasRadarCollar);
			return compound;
		}
		
		
		public EntityDog getDog(World world) {			
			if(!(world instanceof ServerWorld)) {
				DoggyTalentsMod.LOGGER.warn("Something when wrong. Tried to call DogLocation#getDog(PlayerEntity) on what looks like the client side");
				return null;
			}
			
			Entity entity = ((ServerWorld)world).func_217461_a(this.entityId);
			
			if(entity == null) {
				return null;
			}
			else if(!(entity instanceof EntityDog)) {
				DoggyTalentsMod.LOGGER.warn("Something when wrong. The saved dog UUID is not an EntityDog");
				return null;
			}
			
			return (EntityDog)entity;
		}
		
		public LivingEntity getOwner(World world) {
			EntityDog dog = this.getDog(world);
			if(dog != null) {
				return dog.getOwner();
			} else if(this.owner != null) {
				return world.getPlayerByUuid(this.owner);
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
