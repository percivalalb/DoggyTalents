package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.util.IItemProvider;

public class LeatherHelmet extends DyeableAccessory {

    public LeatherHelmet(Supplier<? extends IItemProvider> itemIn) {
        super(DoggyAccessoryTypes.HEAD, itemIn);
    }


    @Override
    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_TOP;
    }
}
