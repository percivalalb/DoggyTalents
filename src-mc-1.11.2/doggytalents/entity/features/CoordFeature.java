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
		return this.dog.hasBedPos();
	}
	
	public boolean hasBowlPos() {
		return this.dog.hasBowlPos();
	}
	
	public void setBedPos(BlockPos pos) {
		this.dog.setBedPos(pos);
	}
	
	public void setBowlPos(BlockPos pos) {
		this.dog.setBowlPos(pos);
	}
	
	public BlockPos getBedPos() {
		return this.dog.getBedPos();
	}
	
	public BlockPos getBowlPos() {
		return this.dog.getBowlPos();
	}
	
	public void resetBowlPosition() {
		this.dog.resetBowlPosition();
	}
	
	@Override
	public void writeAdditional(NBTTagCompound tagCompound) {
		if(this.hasBedPos()) {
			tagCompound.setInteger("bedPosX", this.getBedPos().getX());
			tagCompound.setInteger("bedPosY", this.getBedPos().getY());
			tagCompound.setInteger("bedPosZ", this.getBedPos().getZ());
		}
		
		if(this.hasBowlPos()) {
			tagCompound.setInteger("bowlPosX", this.getBowlPos().getX());
			tagCompound.setInteger("bowlPosY", this.getBowlPos().getY());
			tagCompound.setInteger("bowlPosZ", this.getBowlPos().getZ());
		}
	}
	
	@Override
	public void readAdditional(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("bedPosX")) {
			this.setBedPos(new BlockPos(tagCompound.getInteger("bedPosX"), tagCompound.getInteger("bedPosY"), tagCompound.getInteger("bedPosZ")));
		}
		if(tagCompound.hasKey("bowlPosX")) {
			this.setBowlPos(new BlockPos(tagCompound.getInteger("bowlPosX"), tagCompound.getInteger("bowlPosY"), tagCompound.getInteger("bowlPosZ")));
		}
		
		//Backwards compatibility
		if(tagCompound.hasKey("coords")) {
			String[] split = tagCompound.getString("coords").split(":");
			this.setBedPos(new BlockPos(new Integer(split[0]), new Integer(split[1]), new Integer(split[2])));
			this.setBowlPos(new BlockPos(new Integer(split[3]), new Integer(split[4]), new Integer(split[5])));
		}
	}
}

