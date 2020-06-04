package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.inferface.Accessory;
import doggytalents.api.inferface.AccessoryInstance;
import net.minecraft.util.IItemProvider;

public class Glasses extends Accessory {

    public Glasses(Supplier<? extends IItemProvider> itemIn) {
        super(() -> DoggyAccessoryTypes.GLASSES, itemIn);
    }

    @Override
    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_TOP;
    }
}
