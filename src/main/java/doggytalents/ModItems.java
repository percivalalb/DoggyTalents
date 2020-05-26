package doggytalents;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import doggytalents.item.ItemBigBone;
import doggytalents.item.ItemCapeColoured;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemCreativeOwnerChange;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemDroolBone;
import doggytalents.item.ItemFancyCollar;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemRadarCreative;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTinyBone;
import doggytalents.item.ItemTreat;
import doggytalents.item.ItemTreatBag;
import doggytalents.item.ItemWhistle;
import doggytalents.item.ItemWoolCollar;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<ItemThrowBone> THROW_BONE = registerDryBone("throw_bone");
    public static final RegistryObject<ItemDroolBone> THROW_BONE_WET = registerDrool("throw_bone_wet", THROW_BONE, (prop) -> prop.maxStackSize(1));
    public static final RegistryObject<ItemThrowBone> THROW_STICK = registerDryStick("throw_stick");
    public static final RegistryObject<ItemDroolBone> THROW_STICK_WET = registerDrool("throw_stick_wet", THROW_STICK, (prop) -> prop.maxStackSize(1));
    public static final RegistryObject<Item> TRAINING_TREAT = registerTreat("training_treat", 20);
    public static final RegistryObject<Item> SUPER_TREAT = registerTreat("super_treat", 40);
    public static final RegistryObject<Item> MASTER_TREAT = registerTreat("master_treat", 60);
    public static final RegistryObject<Item> DIRE_TREAT = register("dire_treat", ItemDireTreat::new);
    public static final RegistryObject<Item> BREEDING_BONE = register("breeding_bone");
    public static final RegistryObject<Item> COLLAR_SHEARS = registerWith("collar_shears", (prop) -> prop.maxStackSize(1));
    public static final RegistryObject<Item> DOGGY_CHARM = registerWith("doggy_charm", ItemDoggyCharm::new, 1);
    public static final RegistryObject<Item> RADIO_COLLAR = register("radio_collar");
    public static final RegistryObject<Item> WOOL_COLLAR = register("wool_collar", ItemWoolCollar::new);
    public static final RegistryObject<Item> CREATIVE_COLLAR = registerFancyCollar("creative_collar", ItemFancyCollar.Type.CREATIVE);
    public static final RegistryObject<Item> SPOTTED_COLLAR = registerFancyCollar("spotted_collar", ItemFancyCollar.Type.SPOTTED);
    public static final RegistryObject<Item> MULTICOLOURED_COLLAR = registerFancyCollar("multicoloured_collar", ItemFancyCollar.Type.MULTI_COLOURED);
    public static final RegistryObject<Item> RADAR = registerWith("radar", ItemRadar::new, 1);
    public static final RegistryObject<Item> CREATIVE_RADAR = registerWith("creative_radar", ItemRadarCreative::new, 1);
    public static final RegistryObject<Item> WHISTLE = registerWith("whistle", ItemWhistle::new, 1);
    public static final RegistryObject<Item> TREAT_BAG = registerWith("treat_bag", ItemTreatBag::new, 1);
    public static final RegistryObject<Item> CHEW_STICK = register("chew_stick", ItemChewStick::new);
    public static final RegistryObject<Item> CAPE = register("cape");
    public static final RegistryObject<Item> CAPE_COLOURED = register("cape_coloured", ItemCapeColoured::new);
    public static final RegistryObject<Item> SUNGLASSES = register("sunglasses");
    public static final RegistryObject<Item> LEATHER_JACKET = register("leather_jacket");
    public static final RegistryObject<Item> TINY_BONE = register("tiny_bone", ItemTinyBone::new);
    public static final RegistryObject<Item> BIG_BONE = register("big_bone", ItemBigBone::new);
    public static final RegistryObject<Item> OWNER_CHANGE = registerWith("owner_change", ItemCreativeOwnerChange::new, 1);

    private static Item.Properties createInitialProp() {
        return new Item.Properties().group(ModCreativeTabs.GENERAL);
    }

    private static RegistryObject<ItemThrowBone> registerDryBone(final String name) {
        return register(name, () -> new ItemThrowBone(THROW_BONE_WET, Items.BONE.delegate, createInitialProp().maxStackSize(2)));
    }

    private static RegistryObject<ItemThrowBone> registerDryStick(final String name) {
        return register(name, () -> new ItemThrowBone(THROW_STICK_WET, THROW_STICK, createInitialProp().maxStackSize(8)));
    }

    private static RegistryObject<ItemDroolBone> registerDrool(final String name, RegistryObject<? extends Item> dryBone, Function<Item.Properties, Item.Properties> itemConstructor) {
        return register(name, () -> new ItemDroolBone(dryBone, itemConstructor.apply(createInitialProp())));
    }

    private static RegistryObject<Item> registerTreat(final String name, int maxLevel) {
        return register(name, () -> new ItemTreat(maxLevel, createInitialProp()));
    }

    private static RegistryObject<Item> registerFancyCollar(final String name, ItemFancyCollar.Type type) {
        return register(name, () -> new ItemFancyCollar(type, createInitialProp()));
    }

    private static <T extends Item> RegistryObject<Item> registerWith(final String name, Function<Item.Properties, T> itemConstructor, int maxStackSize) {
        return register(name, () -> itemConstructor.apply(createInitialProp().maxStackSize(maxStackSize)));
    }

    private static <T extends Item> RegistryObject<Item> register(final String name, Function<Item.Properties, T> itemConstructor) {
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
        itemColors.register((stack, tintIndex) -> {
            return stack.hasTag() && stack.getTag().contains("collar_colour") ? stack.getTag().getInt("collar_colour") : -1;
          }, ModItems.WOOL_COLLAR.get());

        itemColors.register((stack, tintIndex) -> {
            return stack.hasTag() && stack.getTag().contains("cape_colour") ? stack.getTag().getInt("cape_colour") : -1;
          }, ModItems.CAPE_COLOURED.get());

        itemColors.register((stack, tintIndex) -> {
             return 4159204;
          }, ModBlocks.DOG_BATH.get());
    }

    public static void tryRegisterColor() {

    }
}