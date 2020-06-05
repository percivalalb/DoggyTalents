package doggytalents.common.network.packet.data;

public class DogObeyData extends DogData {

    public final boolean obeyOthers;

    public DogObeyData(int entityId, boolean obeyOthers) {
        super(entityId);
        this.obeyOthers = obeyOthers;
    }
}
