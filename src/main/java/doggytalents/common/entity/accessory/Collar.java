package doggytalents.common.entity.accessory;

import java.util.function.Supplier;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.world.level.ItemLike;

public class Collar extends Accessory {

    public Collar(Supplier<? extends ItemLike> itemIn) {
        super(DoggyAccessoryTypes.COLLAR, itemIn);
    }

    @Override
    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_TOP;
    }
}
