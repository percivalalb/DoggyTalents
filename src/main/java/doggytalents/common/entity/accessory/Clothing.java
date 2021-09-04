package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessoryTypes;
import doggytalents.api.registry.Accessory;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class Clothing extends Accessory {

    public Clothing(Supplier<? extends ItemLike> itemIn) {
        super(DoggyAccessoryTypes.CLOTHING, itemIn);
    }

}
