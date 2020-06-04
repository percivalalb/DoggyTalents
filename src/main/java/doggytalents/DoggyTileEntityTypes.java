package doggytalents;

import java.util.function.Supplier;

import doggytalents.common.block.tileentity.DogBedTileEntity;
import doggytalents.common.block.tileentity.FoodBowlTileEntity;
import doggytalents.common.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DoggyTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<TileEntityType<DogBedTileEntity>> DOG_BED = register("dog_bed", DogBedTileEntity::new, DoggyBlocks.DOG_BED);
    public static final RegistryObject<TileEntityType<FoodBowlTileEntity>> FOOD_BOWL = register("food_bowl", FoodBowlTileEntity::new, DoggyBlocks.FOOD_BOWL);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(final String name, final Supplier<T> sup, Supplier<? extends Block> validBlock) {
        return register(name, () -> TileEntityType.Builder.create(sup, validBlock.get()).build(null));
    }

    private static <T extends TileEntityType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return TILE_ENTITIES.register(name, sup);
    }
}