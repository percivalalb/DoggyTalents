package doggytalents.api.feature;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.api.registry.Talent;

public interface ITalentFeature {

    /**
     * Gets the level of a talent, defaults to 0 if talent is disabled.
     */
    public int getLevel(@Nullable Talent talentIn);

    /**
     * Sets the level of a talent.
     * Triggers {@link Talent#onSetLevel(IDog, int)}
     */
    public void setLevel(@Nonnull Talent talent, int level);

    /**
     * Sets all talents to level 0.
     * Triggers {@link Talent#onLevelReset(IDog, int)} for every talent
     */
    public void resetTalents();
}
