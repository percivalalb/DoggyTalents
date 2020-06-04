package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Accessory;
import doggytalents.api.inferface.AccessoryType;
import doggytalents.api.inferface.Talent;
import doggytalents.common.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class DoggyRegistries {

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        DoggyTalentsAPI.TALENTS = makeRegistry(Util.getResource("talents"), Talent.class).create();
        DoggyTalentsAPI.ACCESSORIES = makeRegistry(Util.getResource("accessories"), Accessory.class).create();
        DoggyTalentsAPI.ACCESSORY_TYPE = makeRegistry(Util.getResource("accessory_type"), AccessoryType.class).disableSync().create();
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type) {
        return new RegistryBuilder<T>().setName(name).setType(type);
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
