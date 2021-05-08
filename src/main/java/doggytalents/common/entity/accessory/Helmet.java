package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.Accessory;
import net.minecraft.util.IItemProvider;

public class Helmet extends ArmourAccessory {

    public Helmet(Supplier<? extends IItemProvider> itemIn) {
        super(DoggyAccessoryTypes.HEAD, itemIn);
    }

}
