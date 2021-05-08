package doggytalents.common.addon.flamboyant;

import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import doggytalents.api.impl.BeddingMaterial;
import doggytalents.api.impl.CasingMaterial;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.addon.Addon;
import doggytalents.common.util.Util;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class FlamboyantAddon implements Addon {

    public static final String MOD_ID = "flamboyant";

    public static final String[] BLOCKS = {"violet_wool", "slate_grey_wool",
            "sky_blue_wool", "pale_yellow_wool", "pale_pink_wool", "pale_green_wool", "olive_wool",
            "navy_wool", "maroon_wool", "indigo_wool", "hot_pink_wool", "forest_green_wool",
            "dark_green_wool", "cream_wool", "beige_wool", "amber_wool"};

    public final void registerBeddings(final RegistryEvent.Register<IBeddingMaterial> event) {
        if (!this.shouldLoad()) { return; }
        IForgeRegistry<IBeddingMaterial> casingRegistry = event.getRegistry();

        for (String block : BLOCKS) {
            ResourceLocation rl = Util.getResource(MOD_ID, block);
            Supplier<Block> blockGet = () -> ForgeRegistries.BLOCKS.getValue(rl);

            casingRegistry.register(new BeddingMaterial(blockGet).setRegistryName(rl));
        }
    }

    @Override
    public void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(IBeddingMaterial.class, this::registerBeddings);
    }

    @Override
    public Collection<String> getMods() {
        return Lists.newArrayList(MOD_ID);
    }

    @Override
    public String getName() {
        return "Flamboyant Addon";
    }
}
