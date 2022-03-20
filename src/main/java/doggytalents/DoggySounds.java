package doggytalents;

import doggytalents.common.lib.Constants;
import doggytalents.common.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class DoggySounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.Keys.SOUND_EVENTS, Constants.MOD_ID);

    public static final RegistryObject<SoundEvent> WHISTLE_SHORT = register("whistle_short");
    public static final RegistryObject<SoundEvent> WHISTLE_LONG = register("whistle_long");

    private static RegistryObject<SoundEvent> register(final String name) {
        return register(name, SoundEvent::new);
    }

    private static <T extends SoundEvent> RegistryObject<T> register(final String name, final Function<ResourceLocation, T> factory) {
        return register(name, () -> factory.apply(Util.getResource(name)));
    }

    private static <T extends SoundEvent> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return SOUNDS.register(name, sup);
    }
}
