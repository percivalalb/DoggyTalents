package doggytalents;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.registry.Accessory;
import doggytalents.common.entity.accessory.DyeableAccessory;
import doggytalents.common.item.AccessoryItem;
import doggytalents.common.item.ChangeOwnerItem;
import doggytalents.common.item.ChewStickItem;
import doggytalents.common.item.DogShearsItem;
import doggytalents.common.item.DoggyCharmItem;
import doggytalents.common.item.DyeableAccessoryItem;
import doggytalents.common.item.PatrolItem;
import doggytalents.common.item.RadarItem;
import doggytalents.common.item.SizeBoneItem;
import doggytalents.common.item.ThrowableItem;
import doggytalents.common.item.TreatBagItem;
import doggytalents.common.item.TreatItem;
import doggytalents.common.item.WhistleItem;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.Util;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DoggyItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> THROW_BONE = register("throw_bone", () -> new ThrowableItem(Items.BONE.delegate, Items.BONE.delegate, createInitialProp()));
    public static final RegistryObject<Item> THROW_BONE_WET = register("throw_bone_wet");//, THROW_BONE, (prop) -> prop.maxStackSize(1));
    public static final RegistryObject<Item> THROW_STICK = register("throw_stick");
    public static final RegistryObject<Item> THROW_STICK_WET = register("throw_stick_wet");//, THROW_STICK, (prop) -> prop.maxStackSize(1));
    public static final RegistryObject<Item> TRAINING_TREAT = registerTreat("training_treat", DogLevel.Type.NORMAL, 20);
    public static final RegistryObject<Item> SUPER_TREAT = registerTreat("super_treat", DogLevel.Type.NORMAL, 40);
    public static final RegistryObject<Item> MASTER_TREAT = registerTreat("master_treat", DogLevel.Type.NORMAL, 60);
    public static final RegistryObject<Item> DIRE_TREAT = registerTreat("dire_treat", DogLevel.Type.DIRE, 30);
    public static final RegistryObject<Item> BREEDING_BONE = register("breeding_bone");
    public static final RegistryObject<Item> COLLAR_SHEARS = registerWith("collar_shears", DogShearsItem::new, 1);
    public static final RegistryObject<Item> DOGGY_CHARM = registerWith("doggy_charm", DoggyCharmItem::new, 1);
    public static final RegistryObject<AccessoryItem> RADIO_COLLAR = registerAccessory("radio_collar", DoggyAccessories.RADIO_BAND);
    public static final RegistryObject<DyeableAccessoryItem> WOOL_COLLAR = registerAccessoryDyed("wool_collar", DoggyAccessories.DYEABLE_COLLAR);
    public static final RegistryObject<AccessoryItem> CREATIVE_COLLAR = registerAccessory("creative_collar", DoggyAccessories.GOLDEN_COLLAR);
    public static final RegistryObject<AccessoryItem> SPOTTED_COLLAR = registerAccessory("spotted_collar", DoggyAccessories.SPOTTED_COLLAR);
    public static final RegistryObject<AccessoryItem> MULTICOLOURED_COLLAR = registerAccessory("multicoloured_collar", DoggyAccessories.MULTICOLORED_COLLAR);
    public static final RegistryObject<Item> RADAR = registerWith("radar", RadarItem::new, 1);
    public static final RegistryObject<Item> CREATIVE_RADAR = registerWith("creative_radar", RadarItem::new, 1);
    public static final RegistryObject<Item> WHISTLE = registerWith("whistle", WhistleItem::new, 1);
    public static final RegistryObject<Item> TREAT_BAG = registerWith("treat_bag", TreatBagItem::new, 1);
    public static final RegistryObject<Item> CHEW_STICK = register("chew_stick", ChewStickItem::new);
    public static final RegistryObject<AccessoryItem> CAPE = registerAccessory("cape", DoggyAccessories.CAPE);
    public static final RegistryObject<DyeableAccessoryItem> CAPE_COLOURED = registerAccessoryDyed("cape_coloured", DoggyAccessories.DYEABLE_CAPE);
    public static final RegistryObject<AccessoryItem> SUNGLASSES = registerAccessory("sunglasses", DoggyAccessories.SUNGLASSES);
    public static final RegistryObject<AccessoryItem> LEATHER_JACKET = registerAccessory("leather_jacket", DoggyAccessories.LEATHER_JACKET_CLOTHING);
    public static final RegistryObject<Item> TINY_BONE = registerSizeBone("tiny_bone", SizeBoneItem.Type.TINY);
    public static final RegistryObject<Item> BIG_BONE = registerSizeBone("big_bone", SizeBoneItem.Type.BIG);
    public static final RegistryObject<Item> OWNER_CHANGE = registerWith("owner_change", ChangeOwnerItem::new, 1);
    public static final RegistryObject<Item> PATROl = registerWith("patrol_item", PatrolItem::new, 1);

    private static Item.Properties createInitialProp() {
        return new Item.Properties().group(DoggyItemGroups.GENERAL);
    }

    private static RegistryObject<Item> registerSizeBone(final String name, final SizeBoneItem.Type typeIn) {
        return register(name, () -> new SizeBoneItem(typeIn, createInitialProp()));
    }

    private static RegistryObject<Item> registerTreat(final String name, final DogLevel.Type typeIn, int maxLevel) {
        return register(name, () -> new TreatItem(maxLevel, typeIn, createInitialProp()));
    }

    private static RegistryObject<DyeableAccessoryItem> registerAccessoryDyed(final String name, Supplier<? extends DyeableAccessory> type) {
        return register(name, () -> new DyeableAccessoryItem(type, createInitialProp()));
    }

    private static RegistryObject<AccessoryItem> registerAccessory(final String name, Supplier<? extends Accessory> type) {
        return register(name, () -> new AccessoryItem(type, createInitialProp()));
    }

    private static <T extends Item> RegistryObject<T> registerWith(final String name, Function<Item.Properties, T> itemConstructor, int maxStackSize) {
        return register(name, () -> itemConstructor.apply(createInitialProp().maxStackSize(maxStackSize)));
    }

    private static <T extends Item> RegistryObject<T> register(final String name, Function<Item.Properties, T> itemConstructor) {
        return register(name, () -> itemConstructor.apply(createInitialProp()));
    }

    private static RegistryObject<Item> register(final String name) {
        return registerWith(name, (Function<Item.Properties, Item.Properties>) null);
    }

    private static RegistryObject<Item> registerWith(final String name, @Nullable Function<Item.Properties, Item.Properties> extraPropFunc) {
        Item.Properties prop = createInitialProp();
        return register(name, () -> new Item(extraPropFunc != null ? extraPropFunc.apply(prop) : prop));
    }

    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }

    public static void registerItemColours(final ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        Util.acceptOrElse(DoggyItems.WOOL_COLLAR, (item) -> {
            itemColors.register((stack, tintIndex) -> {
                return tintIndex > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack);
             }, item);
        }, DoggyBlocks::logError);

        Util.acceptOrElse(DoggyItems.CAPE_COLOURED, (item) -> {
            itemColors.register((stack, tintIndex) -> {
                return tintIndex > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack);
             }, item);
        }, DoggyBlocks::logError);

        Util.acceptOrElse(DoggyBlocks.DOG_BATH, (item) -> {
            itemColors.register((stack, tintIndex) -> {
                return 4159204;
             }, item);
        }, DoggyBlocks::logError);
    }
}
