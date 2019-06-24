package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    @SubscribeEvent
    public static void newRegistry(RegistryEvent.NewRegistry event) {
        DoggyTalentsAPI.TALENTS = makeRegistry(new ResourceLocation(Reference.MOD_ID, "talents"), Talent.class).disableSync().create();
    }
    
    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type) {
        return new RegistryBuilder<T>().setName(name).setType(type);
    }

}
