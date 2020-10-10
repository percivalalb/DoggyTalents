package doggytalents;

import java.util.function.Supplier;

import doggytalents.api.registry.Accessory;
import doggytalents.common.entity.accessory.Band;
import doggytalents.common.entity.accessory.Clothing;
import doggytalents.common.entity.accessory.Collar;
import doggytalents.common.entity.accessory.DyeableAccessory;
import doggytalents.common.entity.accessory.Glasses;
import doggytalents.common.entity.accessory.Helmet;
import doggytalents.common.entity.accessory.LeatherHelmet;
import doggytalents.common.lib.Constants;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class DoggyAccessories {

    public static final DeferredRegister<Accessory> ACCESSORIES = DeferredRegister.create(Accessory.class, Constants.MOD_ID);

    public static final RegistryObject<DyeableAccessory> DYEABLE_COLLAR = register("dyeable_collar", () -> new DyeableAccessory(DoggyAccessoryTypes.COLLAR, DoggyItems.WOOL_COLLAR));
    public static final RegistryObject<Collar> GOLDEN_COLLAR = register("golden_collar", () -> new Collar(DoggyItems.CREATIVE_COLLAR));
    public static final RegistryObject<Collar> SPOTTED_COLLAR = register("spotted_collar", () -> new Collar(DoggyItems.SPOTTED_COLLAR));
    public static final RegistryObject<Collar> MULTICOLORED_COLLAR = register("multicolored_collar", () -> new Collar(DoggyItems.MULTICOLOURED_COLLAR));

    public static final RegistryObject<Clothing> GUARD_SUIT = register("guard_suit", () -> new Clothing(DoggyItems.GUARD_SUIT));
    public static final RegistryObject<Clothing> LEATHER_JACKET_CLOTHING = register("leather_jacket_clothing", () -> new Clothing(DoggyItems.LEATHER_JACKET));
    public static final RegistryObject<Glasses> SUNGLASSES = register("sunglasses", () -> new Glasses(DoggyItems.SUNGLASSES));
    public static final RegistryObject<Clothing> CAPE = register("cape", () -> new Clothing(DoggyItems.CAPE));
    public static final RegistryObject<DyeableAccessory> DYEABLE_CAPE = register("dyeable_cape", () -> new DyeableAccessory(DoggyAccessoryTypes.CLOTHING, DoggyItems.CAPE_COLOURED));
    public static final RegistryObject<Band> RADIO_BAND = register("radio_band", () -> new Band(DoggyItems.RADIO_COLLAR));

    public static final RegistryObject<Helmet> IRON_HELMET = registerHelmet("iron_helmet", () -> Items.IRON_HELMET);
    public static final RegistryObject<Helmet> DIAMOND_HELMET = registerHelmet("diamond_helmet", () -> Items.DIAMOND_HELMET);
    public static final RegistryObject<LeatherHelmet> LEATHER_HELMET = register("leather_helmet", () -> new LeatherHelmet(Items.LEATHER_HELMET.delegate));
    public static final RegistryObject<Helmet> GOLDEN_HELMET = registerHelmet("golden_helmet", () -> Items.GOLDEN_HELMET);
    public static final RegistryObject<Helmet> CHAINMAIL_HELMET = registerHelmet("chainmail_helmet", () -> Items.CHAINMAIL_HELMET);
    public static final RegistryObject<Helmet> TURTLE_HELMET = registerHelmet("turtle_helmet", () -> Items.TURTLE_HELMET);
    public static final RegistryObject<Helmet> NETHERITE_HELMET = registerHelmet("netherite_helmet", () -> Items.NETHERITE_HELMET);

    private static RegistryObject<Helmet> registerHelmet(final String name, final Supplier<? extends IItemProvider> itemIn) {
        return ACCESSORIES.register(name, () -> new Helmet(itemIn));
    }

    private static <T extends Accessory> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ACCESSORIES.register(name, sup);
    }
}