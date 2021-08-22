package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.impl.MissingBeddingMaterial;
import doggytalents.api.impl.MissingCasingMissing;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.api.registry.Talent;
import doggytalents.common.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class DoggyRegistries {

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        DoggyTalentsAPI.TALENTS = makeRegistry("talents", Talent.class).create();
        DoggyTalentsAPI.ACCESSORIES = makeRegistry("accessories", Accessory.class).create();
        DoggyTalentsAPI.ACCESSORY_TYPE = makeRegistry("accessory_type", AccessoryType.class).disableSync().create();
        DoggyTalentsAPI.BEDDING_MATERIAL = makeRegistry("bedding", IBeddingMaterial.class).addCallback(BeddingCallbacks.INSTANCE).create();
        DoggyTalentsAPI.CASING_MATERIAL = makeRegistry("casing", ICasingMaterial.class).addCallback(CasingCallbacks.INSTANCE).create(); //TODO ADD holder object
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(final String name, Class<T> type) {
        return new RegistryBuilder<T>().setName(Util.getResource(name)).setType(type);
    }

    private static class BeddingCallbacks implements IForgeRegistry.DummyFactory<IBeddingMaterial> {

        static final BeddingCallbacks INSTANCE = new BeddingCallbacks();

        @Override
        public IBeddingMaterial createDummy(ResourceLocation key) {
            return new MissingBeddingMaterial().setRegistryName(key);
        }
    }

    private static class CasingCallbacks implements IForgeRegistry.DummyFactory<ICasingMaterial> {

        static final CasingCallbacks INSTANCE = new CasingCallbacks();

        @Override
        public ICasingMaterial createDummy(ResourceLocation key) {
            return new MissingCasingMissing().setRegistryName(key);
        }
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
