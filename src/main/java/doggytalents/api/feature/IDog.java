package doggytalents.api.feature;

import net.minecraft.entity.passive.TameableEntity;

public interface IDog {

    public ICoordFeature getCoordFeature();
    public IGenderFeature getGenderFeature();
    public ILevelFeature getLevelFeature();
    public IModeFeature getModeFeature();
    public IStatsFeature getStatsFeature();
    public ITalentFeature getTalentFeature();
    
    public TameableEntity getDog();
}
