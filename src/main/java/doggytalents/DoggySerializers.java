package doggytalents;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.entity.serializers.*;
import doggytalents.common.lib.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DoggySerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    // Serializers can be static. They just define how the encoding should take place. They do not need to be renewed
    // on each refresh of the ENTITY_DATA_SERIALIZERS registry.
    public static final EntityDataSerializer<List<TalentInstance>> TALENT_SERIALIZER = new TalentListSerializer();
    public static final EntityDataSerializer<Optional<AccessoryInstance>> COLLAR_TYPE_SERIALIZER = new CollarSerializer();
    public static final EntityDataSerializer<List<AccessoryInstance>> ACCESSORY_SERIALIZER = new AccessorySerializer();
    public static final EntityDataSerializer<EnumGender> GENDER_SERIALIZER = new GenderSerializer();
    public static final EntityDataSerializer<EnumMode> MODE_SERIALIZER = new ModeSerializer();
    public static final EntityDataSerializer<DogLevel> DOG_LEVEL_SERIALIZER = new DogLevelSerializer();
    public static final EntityDataSerializer<DimensionDependantArg<Optional<BlockPos>>> BED_LOC_SERIALIZER = new BedLocationsSerializer<>();


    private static RegistryObject<EntityDataSerializer> register(final String name, final Supplier<EntityDataSerializer> sup) {
        return SERIALIZERS.register(name, sup);
    }

    static {
        register("talents", () -> TALENT_SERIALIZER);
        register("collar", () -> COLLAR_TYPE_SERIALIZER);
        register("accessories", () -> ACCESSORY_SERIALIZER);
        register("gender", () -> GENDER_SERIALIZER);
        register("mode", () -> MODE_SERIALIZER);
        register("dog_level", () -> DOG_LEVEL_SERIALIZER);
        register("dog_bed_location", () -> BED_LOC_SERIALIZER);
    }
}
