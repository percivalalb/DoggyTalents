package doggytalents.common.network.packet.data;

import doggytalents.api.feature.EnumMode;

public class DogModeData extends DogData {

    public EnumMode mode;

    public DogModeData(int entityId, EnumMode modeIn) {
        super(entityId);
        this.mode = modeIn;
    }
}
