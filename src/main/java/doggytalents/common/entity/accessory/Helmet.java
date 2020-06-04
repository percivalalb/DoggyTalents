package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.inferface.Accessory;
import net.minecraft.util.IItemProvider;

public class Helmet extends Accessory {

    public Helmet(Supplier<? extends IItemProvider> itemIn) {
        super(() -> DoggyAccessoryTypes.HEAD, itemIn);
    }

}
