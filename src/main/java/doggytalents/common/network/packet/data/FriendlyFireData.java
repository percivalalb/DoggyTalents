package doggytalents.common.network.packet.data;

public class FriendlyFireData extends DogData {

    public final boolean friendlyFire;

    public FriendlyFireData(int entityId, boolean friendlyFire) {
        super(entityId);
        this.friendlyFire = friendlyFire;
    }
}
