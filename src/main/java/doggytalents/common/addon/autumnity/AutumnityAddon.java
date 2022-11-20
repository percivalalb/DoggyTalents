package doggytalents.common.addon.autumnity;

import com.google.common.collect.Lists;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.impl.CasingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.addon.Addon;
import doggytalents.common.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.function.Supplier;

public class AutumnityAddon implements Addon {

    public static final String MOD_ID = "autumnity";

    public static final String[] BLOCKS = {"maple_planks"};

    public static final DeferredRegister<ICasingMaterial> CASINGS = DeferredRegister.create(DoggyTalentsAPI.RegistryKeys.CASING_REGISTRY, MOD_ID);

    @Override
    public void init() {
        if (!this.shouldLoad()) { return; }

        for (String block : BLOCKS) {
            ResourceLocation rl = Util.getResource(MOD_ID, block);
            Supplier<Block> blockGet = () -> ForgeRegistries.BLOCKS.getValue(rl);

            CASINGS.register(rl.getPath(), () -> new CasingMaterial(blockGet.get().builtInRegistryHolder()));
        }

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(CASINGS);
    }

    @Override
    public Collection<String> getMods() {
        return Lists.newArrayList(MOD_ID);
    }

    @Override
    public String getName() {
        return "Autumnity Addon";
    }
}
