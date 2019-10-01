package doggytalents.entity.features;

import javax.annotation.Nullable;

import doggytalents.api.feature.ICoordFeature;
import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * @author ProPercivalalb
 **/
public class CoordFeature extends DogFeature implements ICoordFeature {

    public CoordFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public boolean hasBedPos() {
        return this.dog.hasBedPos();
    }

    @Override
    public boolean hasBowlPos() {
        return this.dog.hasBowlPos();
    }

    @Override
    public void setBedPos(@Nullable BlockPos pos) {
        this.dog.setBedPos(pos);
    }

    @Override
    public void setBowlPos(@Nullable BlockPos pos) {
        this.dog.setBowlPos(pos);
    }

    @Override
    public BlockPos getBedPos() {
        return this.dog.getBedPos();
    }

    @Override
    public BlockPos getBowlPos() {
        return this.dog.getBowlPos();
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
        if(tagCompound.contains("bedPosX", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.setBedPos(new BlockPos(tagCompound.getInt("bedPosX"), tagCompound.getInt("bedPosY"), tagCompound.getInt("bedPosZ")));
        }
        if(tagCompound.contains("bowlPosX", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.setBowlPos(new BlockPos(tagCompound.getInt("bowlPosX"), tagCompound.getInt("bowlPosY"), tagCompound.getInt("bowlPosZ")));
        }

        //Backwards compatibility
        if(tagCompound.contains("coords", Constants.NBT.TAG_STRING)) {
            String[] split = tagCompound.getString("coords").split(":");
            this.setBedPos(new BlockPos(new Integer(split[0]), new Integer(split[1]), new Integer(split[2])));
            this.setBowlPos(new BlockPos(new Integer(split[3]), new Integer(split[4]), new Integer(split[5])));
        }
    }
}

