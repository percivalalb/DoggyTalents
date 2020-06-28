package doggytalents.common.addon.botania;

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

public class BotaniaAddon implements Addon {

    public static final String MOD_ID = "botania";

    public static final String[] BLOCKS = {"livingwood_planks", "mossy_livingwood_planks",
            "dreamwood_planks", "mossy_dreamwood_planks", "shimmerwood_planks"};

    public static final String[] TEXTURES = {"livingwood1", "livingwood2",
            "dreamwood1", "dreamwood2", "shimmerwoodplanks"};

    public final void registerCasings(final RegistryEvent.Register<ICasingMaterial> event) {
        if (!this.shouldLoad()) { return; }
        IForgeRegistry<ICasingMaterial> casingRegistry = event.getRegistry();

        int i = 0;
        for (String block : BLOCKS) {
            ResourceLocation rl = Util.getResource(MOD_ID, block);
            ResourceLocation texture = Util.getResource(MOD_ID, "blocks/" + TEXTURES[i]);
            Supplier<Block> blockGet = () -> ForgeRegistries.BLOCKS.getValue(rl);

            casingRegistry.register(new CasingMaterial(blockGet, texture).setRegistryName(rl));
            i++;
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
        return "Botania Addon";
    }
}
