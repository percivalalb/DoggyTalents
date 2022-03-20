package doggytalents;

import doggytalents.common.lib.Constants;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DoggyAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, Constants.MOD_ID);

    public static final RegistryObject<Attribute> JUMP_POWER = register("generic.jump_power", () -> new RangedAttribute("attribute.name.generic.jump_power", 0.0D, 0.0D, 1.0D).setSyncable(true));
    public static final RegistryObject<Attribute> CRIT_CHANCE = register("generic.crit_chance", () -> new RangedAttribute("attribute.name.generic.crit_chance", 0.0D, 0.0D, 1.0D));
    public static final RegistryObject<Attribute> CRIT_BONUS = register("generic.crit_bonus", () -> new RangedAttribute("attribute.name.generic.crit_bonus", 0.0D, 0.0D, 1.0D));

    private static <T extends Attribute> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ATTRIBUTES.register(name, sup);
    }
}

