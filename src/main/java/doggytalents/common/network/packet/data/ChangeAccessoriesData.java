package doggytalents.common.network.packet.data;

public class ChangeAccessoriesData extends DogData {
    
    public boolean add;
    public int slotId;

    public ChangeAccessoriesData(int entityId, boolean add, 
        int slotId) {
        super(entityId);
        this.add = add;
        this.slotId = slotId;
    }

}
