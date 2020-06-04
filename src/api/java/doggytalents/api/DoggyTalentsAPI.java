package doggytalents.api;

import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.BeddingMaterial;
import doggytalents.api.registry.CasingMaterial;
import doggytalents.api.registry.Talent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author ProPercivalalb
 */
public class DoggyTalentsAPI {

    public static IForgeRegistry<Talent> TALENTS;
    public static IForgeRegistry<Accessory> ACCESSORIES;
    public static IForgeRegistry<AccessoryType> ACCESSORY_TYPE;
    public static IForgeRegistry<BeddingMaterial> BEDDING_MATERIAL;
    public static IForgeRegistry<CasingMaterial> CASING_MATERIAL;
}
