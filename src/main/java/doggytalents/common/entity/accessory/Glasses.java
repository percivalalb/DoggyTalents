package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class Glasses extends Accessory {

    public Glasses(Supplier<? extends ItemLike> itemIn) {
        super(DoggyAccessoryTypes.GLASSES, itemIn);
    }

    @Override
    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_TOP;
    }
}
