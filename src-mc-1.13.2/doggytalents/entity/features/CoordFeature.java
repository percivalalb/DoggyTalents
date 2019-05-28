package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author ProPercivalalb
 **/
public class CoordFeature extends DogFeature {
	
	public CoordFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	public boolean hasBedPos() {
		return this.dog.dataTracker.hasBedPos();
	}
	
	public boolean hasBowlPos() {
		return this.dog.dataTracker.hasBowlPos();
	}
	
	public void setBedPos(BlockPos pos) {
		this.dog.dataTracker.setBedPos(pos);
	}
	
	public void setBowlPos(BlockPos pos) {
		this.dog.dataTracker.setBowlPos(pos);
	}
	
	public BlockPos getBedPos() {
		return this.dog.dataTracker.getBedPos();
	}
	
	public BlockPos getBowlPos() {
		return this.dog.dataTracker.getBowlPos();
	}
	
	public void resetBowlPosition() {
		this.dog.dataTracker.resetBowlPosition();
	}
	
	@Override
	public void writeAdditional(NBTTagCompound tagCompound) {
		if(this.hasBedPos()) {
			tagCompound.putInt("bedPosX", this.getBedPos().getX());
			tagCompound.putInt("bedPosY", this.getBedPos().getY());
			tagCompound.putInt("bedPosZ", this.getBedPos().getZ());
		}
		
		if(this.hasBowlPos()) {
			tagCompound.putInt("bowlPosX", this.getBowlPos().getX());
			tagCompound.putInt("bowlPosY", this.getBowlPos().getY());
			tagCompound.putInt("bowlPosZ", this.getBowlPos().getZ());
		}
	}
	
	@Override
	public void readAdditional(NBTTagCompound tagCompound) {
		if(tagCompound.contains("bedPosX")) {
			this.setBedPos(new BlockPos(tagCompound.getInt("bedPosX"), tagCompound.getInt("bedPosY"), tagCompound.getInt("bedPosZ")));
		}
		if(tagCompound.contains("bowlPosX")) {
			this.setBowlPos(new BlockPos(tagCompound.getInt("bowlPosX"), tagCompound.getInt("bowlPosY"), tagCompound.getInt("bowlPosZ")));
		}
		
		//Backwards compatibility
		if(tagCompound.contains("coords")) {
			String[] split = tagCompound.getString("coords").split(":");
			this.setBedPos(new BlockPos(new Integer(split[0]), new Integer(split[1]), new Integer(split[2])));
			this.setBowlPos(new BlockPos(new Integer(split[3]), new Integer(split[4]), new Integer(split[5])));
		}
	}
}

