package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessoryTypes;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class Helmet extends ArmourAccessory {

    public Helmet(Supplier<? extends ItemLike> itemIn) {
        super(DoggyAccessoryTypes.HEAD, itemIn);
    }

}
