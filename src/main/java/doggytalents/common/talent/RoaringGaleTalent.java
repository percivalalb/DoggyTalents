package doggytalents.common.talent;

import doggytalents.api.feature.DataKey;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;

public class RoaringGaleTalent extends TalentInstance {

    public static DataKey<Integer> COOLDOWN = DataKey.make();

    public RoaringGaleTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }
}
