package doggytalents;

import doggytalents.common.block.DogBathBlock;
import doggytalents.common.block.DogBedBlock;
import doggytalents.common.block.FoodBowlBlock;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.Util;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoggyBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DoggyItems.ITEMS;

    public static final RegistryObject<DogBedBlock> DOG_BED = registerWithItem("dog_bed", DogBedBlock::new, (prop) -> prop.tab(DoggyItemGroups.DOG_BED));
    public static final RegistryObject<DogBathBlock> DOG_BATH = registerWithItem("dog_bath", DogBathBlock::new);
    public static final RegistryObject<FoodBowlBlock> FOOD_BOWL = registerWithItem("food_bowl", FoodBowlBlock::new);

    private static Item.Properties createInitialProp() {
        return new Item.Properties().tab(DoggyItemGroups.GENERAL);
    }

    private static BlockItem makeItemBlock(Block block) {
        return makeItemBlock(block, null);
    }

    private static BlockItem makeItemBlock(Block block, @Nullable Function<Item.Properties, Item.Properties> extraPropFunc) {
        Item.Properties prop = createInitialProp();
        return new BlockItem(block, extraPropFunc != null ? extraPropFunc.apply(prop) : prop);
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(final String name, final Supplier<T> blockSupplier, @Nullable Function<Item.Properties, Item.Properties> extraPropFunc) {
        return register(name, blockSupplier, (b) -> makeItemBlock(b.get(), extraPropFunc));
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(final String name, final Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (b) -> makeItemBlock(b.get()));
    }

    private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<T> blockSupplier, final Function<RegistryObject<T>, Item> itemFunction) {
        RegistryObject<T> blockObj = register(name, blockSupplier);
        ITEMS.register(name, () -> itemFunction.apply(blockObj));
        return blockObj;
    }

    private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }

    public static void registerBlockColours(final ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();

        Util.acceptOrElse(DoggyBlocks.DOG_BATH, (block) -> {
            blockColors.register((state, world, pos, tintIndex) -> {
                return world != null && pos != null ? BiomeColors.getAverageWaterColor(world, pos) : -1;
             }, block);
        }, DoggyBlocks::logError);
    }

    public static void logError() {
        // Only try to register if blocks were successfully registered
        // Trying to avoid as reports like DoggyTalents#242, where it says
        // DoggyTalents crashed but is not the CAUSE of the crash

         DoggyTalents2.LOGGER.info("Items/Blocks were not registered for some reason... probably beacuse we are c...r..a..s.hing");
    }
}
