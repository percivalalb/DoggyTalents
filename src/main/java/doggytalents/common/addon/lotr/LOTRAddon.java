package doggytalents.common.addon.lotr;

import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import doggytalents.api.impl.CasingMaterial;
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

public class LOTRAddon implements Addon {

    public static final String MOD_ID = "lotr";

    public static final String[] BLOCKS = {"pine_planks", "mallorn_planks",
            "mirk_oak_planks", "charred_planks", "apple_planks", "pear_planks",
            "cherry_planks","lebethron_planks", "beech_planks", "maple_planks",
            "aspen_planks", "lairelosse_planks", "cedar_planks", "fir_planks",
            "larch_planks", "holly_planks", "green_oak_planks"};

    public final void registerCasings(final RegistryEvent.Register<ICasingMaterial> event) {
        if (!this.shouldLoad()) { return; }
        IForgeRegistry<ICasingMaterial> casingRegistry = event.getRegistry();

        for (String block : BLOCKS) {
            ResourceLocation rl = Util.getResource(MOD_ID, block);
            Supplier<Block> blockGet = () -> ForgeRegistries.BLOCKS.getValue(rl);

            casingRegistry.register(new CasingMaterial(blockGet).setRegistryName(rl));
        }
    }

    @Override
    public void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(ICasingMaterial.class, this::registerCasings);
    }

    @Override
    public Collection<String> getMods() {
        return Lists.newArrayList(MOD_ID);
    }

    @Override
    public String getName() {
        return "Lotr Addon";
    }
}
