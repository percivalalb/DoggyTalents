package doggytalents.common.network.packet.data;

public class DogTextureData extends DogData {

    public String hash;

    public DogTextureData(int entityId, String hash) {
        super(entityId);
        this.hash = hash;
    }

}
