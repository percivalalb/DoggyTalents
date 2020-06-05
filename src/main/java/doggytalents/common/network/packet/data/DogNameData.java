package doggytalents.common.network.packet.data;

public class DogNameData extends DogData {

    public final String name;

    public DogNameData(int entityId, String name) {
        super(entityId);
        this.name = name;
    }
}
