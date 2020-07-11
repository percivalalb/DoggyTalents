package doggytalents.common.storage;

import java.util.UUID;

public interface IDogData {

    public UUID getDogId();

    public UUID getOwnerId();

    public String getDogName();

    public String getOwnerName();
}
