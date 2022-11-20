package doggytalents.api;

import doggytalents.api.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * @author ProPercivalalb
 */
public class DoggyTalentsAPI {

    public static Supplier<IForgeRegistry<Talent>> TALENTS;
    public static Supplier<IForgeRegistry<Accessory>> ACCESSORIES;
    public static Supplier<IForgeRegistry<AccessoryType>> ACCESSORY_TYPE;
    public static Supplier<IForgeRegistry<IBeddingMaterial>> BEDDING_MATERIAL;
    public static Supplier<IForgeRegistry<ICasingMaterial>> CASING_MATERIAL;

    public static final String MOD_ID = "doggytalents";

    public class RegistryKeys {
        public static final ResourceLocation TALENTS_REGISTRY = new ResourceLocation(MOD_ID, "talents");
        public static final ResourceLocation ACCESSORIES_REGISTRY = new ResourceLocation(MOD_ID, "accessories");
        public static final ResourceLocation ACCESSORY_TYPE_REGISTRY = new ResourceLocation(MOD_ID, "accessory_type");
        public static final ResourceLocation BEDDING_REGISTRY = new ResourceLocation(MOD_ID, "bedding");
        public static final ResourceLocation CASING_REGISTRY = new ResourceLocation(MOD_ID, "casing");
    }


    public static final Logger LOGGER = LogManager.getLogger("doggytalents");
}
