package doggytalents.entity;

import com.google.common.base.Optional;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author ProPercivalalb
 **/
public class CoordUtil {

	public EntityDog dog;
	
	public CoordUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public boolean hasBedPos() {
		return this.dog.getDataManager().get(EntityDog.BED_POS).isPresent();
	}
	
	public boolean hasBowlPos() {
		return this.dog.getDataManager().get(EntityDog.BOWL_POS).isPresent();
	}
	
	public BlockPos getBedPos() {
		return this.dog.getDataManager().get(EntityDog.BED_POS).get();
	}
	
	public BlockPos getBowlPos() {
		return this.dog.getDataManager().get(EntityDog.BOWL_POS).get();
	}

	public void resetBedPosition() {
		this.dog.getDataManager().set(EntityDog.BED_POS, Optional.absent());
	}
	
	public void setBedPos(BlockPos pos) {
		this.dog.getDataManager().set(EntityDog.BED_POS, Optional.fromNullable(pos));
	}
	
	public void setBowlPos(BlockPos pos) {
		this.dog.getDataManager().set(EntityDog.BOWL_POS, Optional.fromNullable(pos));
	}

	
	public void writeToNBT(NBTTagCompound tagCompound) {
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
	
	public void readFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("bedPosX"))
			this.setBedPos(new BlockPos(tagCompound.getInteger("bedPosX"), tagCompound.getInteger("bedPosY"), tagCompound.getInteger("bedPosZ")));
		
		if(tagCompound.hasKey("bowlPosX"))
			this.setBowlPos(new BlockPos(tagCompound.getInteger("bowlPosX"), tagCompound.getInteger("bowlPosY"), tagCompound.getInteger("bowlPosZ")));
	}
}

