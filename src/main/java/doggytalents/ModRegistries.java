package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModRegistries {

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        DoggyTalentsAPI.TALENTS = makeRegistry(new ResourceLocation(Reference.MOD_ID, "talents"), Talent.class).create();
    }
    
    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type) {
        return new RegistryBuilder<T>().setName(name).setType(type);
    }

}
