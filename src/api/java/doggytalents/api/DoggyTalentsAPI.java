package doggytalents.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.api.registry.Talent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author ProPercivalalb
 */
public class DoggyTalentsAPI {

    public static IForgeRegistry<Talent> TALENTS;
    public static IForgeRegistry<Accessory> ACCESSORIES;
    public static IForgeRegistry<AccessoryType> ACCESSORY_TYPE;
    public static IForgeRegistry<IBeddingMaterial> BEDDING_MATERIAL;
    public static IForgeRegistry<ICasingMaterial> CASING_MATERIAL;

    public static final Logger LOGGER = LogManager.getLogger("doggytalents");
}
