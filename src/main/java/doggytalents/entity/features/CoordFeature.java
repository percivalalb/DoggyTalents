package doggytalents.entity.features;

import doggytalents.api.feature.ICoordFeature;
import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

/**
 * @author ProPercivalalb
 **/
public class CoordFeature extends DogFeature implements ICoordFeature {
    
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
    public void writeAdditional(CompoundNBT tagCompound) {
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
    public void readAdditional(CompoundNBT tagCompound) {
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

