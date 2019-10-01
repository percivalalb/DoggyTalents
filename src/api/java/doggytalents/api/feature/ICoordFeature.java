package doggytalents.api.feature;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public interface ICoordFeature {

    /**
     * Returns true if the dogs owner has a bed spawn point
     * @return If the dogs owner has a bed spawn point
     */
    public boolean hasBedPos();

    /**
     * Returns the owners bed position, if no bed position is set
     * then the worlds spawn point is given. So a check to
     * {@link #hasBedPos()}} before hand is probably required.
     * @return The owners bed pos
     */
    public @Nonnull BlockPos getBedPos();

    /**
     * Sets the owners bed position. If null is given it resets
     * the bed position
     * @param pos The owners bed position
     */
    public void setBedPos(@Nullable BlockPos pos);

    /**
     * Returns true if the dog has a bowl set
     * @return If the dog has a bowl set
     */
    public boolean hasBowlPos();

    /**
     * Returns the dogs bowl position, if no bowl position is set
     * then the dogs position is returned. So a check to
     * {@link #hasBowlPos()}} before hand is probably required.
     * @return The owners bed pos
     */
    public @Nonnull BlockPos getBowlPos();


    /**
     * Sets the dogs bowl position. If null is given it resets
     * the bowl position
     * @param pos The owners bed position
     */
    public void setBowlPos(@Nullable BlockPos pos);
}
