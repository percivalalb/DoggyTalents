package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.Talent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class DoggyRegistries {

    public static void newRegistry(NewRegistryEvent event) {
        DoggyTalentsAPI.TALENTS = event.create(makeRegistry(DoggyTalentsAPI.RegistryKeys.TALENTS_REGISTRY, Talent.class));
        DoggyTalentsAPI.ACCESSORIES = event.create(makeRegistry(DoggyTalentsAPI.RegistryKeys.ACCESSORIES_REGISTRY, Accessory.class));
        DoggyTalentsAPI.ACCESSORY_TYPE = event.create(makeRegistry(DoggyTalentsAPI.RegistryKeys.ACCESSORY_TYPE_REGISTRY, AccessoryType.class).disableSync());
    }

    private static <T> RegistryBuilder<T> makeRegistry(final ResourceLocation rl, Class<T> type) {
        return new RegistryBuilder<T>().setName(rl);
    }


//
//    private static class AccessoryCallbacks implements IForgeRegistry.DummyFactory<Accessory> {
//
//        static final AccessoryCallbacks INSTANCE = new AccessoryCallbacks();
//
//        @Override
//        public Accessory createDummy(ResourceLocation key) {
//            return new Accessory(() -> DoggyAccessoryTypes.CLOTHING).setRegistryName(key);
//        }
//    }
//
//    private static class AccessoryTypeCallbacks implements IForgeRegistry.DummyFactory<AccessoryType> {
//
//        static final AccessoryTypeCallbacks INSTANCE = new AccessoryTypeCallbacks();
//        static final AccessoryType dummyType = new AccessoryType();
//
//        @Override
//        public AccessoryType createDummy(ResourceLocation key) {
//            return this.dummyType.set;
//        }
//
//    }
}
