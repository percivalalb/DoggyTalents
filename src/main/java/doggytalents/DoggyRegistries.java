package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.impl.MissingBeddingMaterial;
import doggytalents.api.impl.MissingCasingMissing;
import doggytalents.api.registry.*;
import doggytalents.common.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class DoggyRegistries {

    protected class Keys {
        public static final ResourceLocation TALENTS_REGISTRY = Util.getResource("talents");
        public static final ResourceLocation ACCESSORIES_REGISTRY = Util.getResource("accessories");
        public static final ResourceLocation ACCESSORY_TYPE_REGISTRY = Util.getResource("accessory_type");
        public static final ResourceLocation BEDDING_REGISTRY = Util.getResource("bedding");
        public static final ResourceLocation CASING_REGISTRY = Util.getResource("casing");
    }

    public static void newRegistry(NewRegistryEvent event) {
        DoggyTalentsAPI.TALENTS = event.create(makeRegistry(Keys.TALENTS_REGISTRY, Talent.class));
        DoggyTalentsAPI.ACCESSORIES = event.create(makeRegistry(Keys.ACCESSORIES_REGISTRY, Accessory.class));
        DoggyTalentsAPI.ACCESSORY_TYPE = event.create(makeRegistry(Keys.ACCESSORY_TYPE_REGISTRY, AccessoryType.class).disableSync());
        DoggyTalentsAPI.BEDDING_MATERIAL = event.create(makeRegistry(Keys.BEDDING_REGISTRY, IBeddingMaterial.class).addCallback(BeddingCallbacks.INSTANCE));
        DoggyTalentsAPI.CASING_MATERIAL = event.create(makeRegistry(Keys.CASING_REGISTRY, ICasingMaterial.class).addCallback(CasingCallbacks.INSTANCE)); //TODO ADD holder object
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(final ResourceLocation rl, Class<T> type) {
        return new RegistryBuilder<T>().setName(rl).setType(type);
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
