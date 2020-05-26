package doggytalents;

import java.util.function.Supplier;

import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntityDogBed>> DOG_BED = register("dog_bed", TileEntityDogBed::new, ModBlocks.DOG_BED);
    public static final RegistryObject<TileEntityType<TileEntityFoodBowl>> FOOD_BOWL = register("food_bowl", TileEntityFoodBowl::new, ModBlocks.FOOD_BOWL);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(final String name, final Supplier<T> sup, Supplier<? extends Block> validBlock) {
        return register(name, () -> TileEntityType.Builder.create(sup, validBlock.get()).build(null));
    }

    private static <T extends TileEntityType<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return TILE_ENTITIES.register(name, sup);
    }
}