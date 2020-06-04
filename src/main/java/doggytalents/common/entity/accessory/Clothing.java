package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.Accessory;
import net.minecraft.util.IItemProvider;

public class Clothing extends Accessory {

    public Clothing(Supplier<? extends IItemProvider> itemIn) {
        super(() -> DoggyAccessoryTypes.CLOTHING, itemIn);
    }

}
