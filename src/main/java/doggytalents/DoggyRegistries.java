package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.BeddingMaterial;
import doggytalents.api.registry.CasingMaterial;
import doggytalents.api.registry.Talent;
import doggytalents.common.util.Util;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class DoggyRegistries {

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        DoggyTalentsAPI.TALENTS = makeRegistry("talents", Talent.class).create();
        DoggyTalentsAPI.ACCESSORIES = makeRegistry("accessories", Accessory.class).create();
        DoggyTalentsAPI.ACCESSORY_TYPE = makeRegistry("accessory_type", AccessoryType.class).disableSync().create();
        DoggyTalentsAPI.BEDDING_MATERIAL = makeRegistry("bedding", BeddingMaterial.class).create();
        DoggyTalentsAPI.CASING_MATERIAL = makeRegistry("casing", CasingMaterial.class).create(); //TODO ADD holder object
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(final String name, Class<T> type) {
        return new RegistryBuilder<T>().setName(Util.getResource(name)).setType(type);
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