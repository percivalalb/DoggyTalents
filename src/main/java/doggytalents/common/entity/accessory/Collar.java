package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.inferface.Accessory;
import doggytalents.api.inferface.AccessoryInstance;
import net.minecraft.util.IItemProvider;

public class Collar extends Accessory {

    public Collar(Supplier<? extends IItemProvider> itemIn) {
        super(() -> DoggyAccessoryTypes.COLLAR, itemIn);
    }

    @Override
    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_TOP;
    }
}
