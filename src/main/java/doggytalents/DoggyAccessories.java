package doggytalents;

import doggytalents.api.inferface.Accessory;
import doggytalents.common.entity.accessory.Helmet;
import doggytalents.common.entity.accessory.Band;
import doggytalents.common.entity.accessory.Clothing;
import doggytalents.common.entity.accessory.Collar;
import doggytalents.common.entity.accessory.DyeableAccessory;
import doggytalents.common.entity.accessory.Glasses;
import doggytalents.common.entity.accessory.LeatherHelmet;
import doggytalents.common.lib.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MOD_ID)
public class DoggyAccessories {

    public static final DyeableAccessory DYEABLE_COLLAR = null;
    public static final Collar GOLDEN_COLLAR = null;
    public static final Collar SPOTTED_COLLAR = null;
    public static final Collar MULTICOLORED_COLLAR = null;

    public static final Clothing LEATHER_JACKET_CLOTHING = null;
    public static final Glasses SUNGLASSES = null;
    public static final Clothing CAPE = null;
    public static final DyeableAccessory DYEABLE_CAPE = null;
    public static final Band RADIO_BAND = null;

    public static final Helmet IRON_HELMET = null;
    public static final Helmet DIAMOND_HELMET = null;
    public static final LeatherHelmet LEATHER_HELMET = null;
    public static final Helmet GOLDEN_HELMET = null;
    public static final Helmet CHAINMAIL_HELMET = null;
    public static final Helmet TURTLE_HELMET = null;

    public static final void registerAccessories(final RegistryEvent.Register<Accessory> event) {
        IForgeRegistry<Accessory> accessoryRegistry = event.getRegistry();

        accessoryRegistry.register(new DyeableAccessory(() -> DoggyAccessoryTypes.COLLAR, DoggyItems.WOOL_COLLAR).setRegistryName("dyeable_collar"));
        accessoryRegistry.register(new Collar(DoggyItems.CREATIVE_COLLAR).setRegistryName("golden_collar"));
        accessoryRegistry.register(new Collar(DoggyItems.SPOTTED_COLLAR).setRegistryName("spotted_collar"));
        accessoryRegistry.register(new Collar(DoggyItems.MULTICOLOURED_COLLAR).setRegistryName("multicolored_collar"));

        accessoryRegistry.register(new Clothing(DoggyItems.CAPE).setRegistryName("cape"));
        accessoryRegistry.register(new DyeableAccessory(() -> DoggyAccessoryTypes.CLOTHING, DoggyItems.CAPE_COLOURED).setRegistryName("dyeable_cape"));
        accessoryRegistry.register(new Clothing(DoggyItems.LEATHER_JACKET).setRegistryName("leather_jacket_clothing"));

        accessoryRegistry.register(new Glasses(DoggyItems.SUNGLASSES).setRegistryName("sunglasses"));

        accessoryRegistry.register(new Band(DoggyItems.RADIO_COLLAR).setRegistryName("radio_band"));

        Item[] helmets = new Item[] {Items.DIAMOND_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.TURTLE_HELMET};

        for (Item helmet : helmets) {
            accessoryRegistry.register(new Helmet(helmet.delegate).setRegistryName(helmet.getRegistryName().getPath()));
        }

        accessoryRegistry.register(new LeatherHelmet(Items.LEATHER_HELMET.delegate).setRegistryName("leather_helmet"));
    }
}