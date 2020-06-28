package doggytalents;

import java.util.function.Supplier;

import doggytalents.api.registry.Talent;
import doggytalents.common.lib.Constants;
import doggytalents.common.talent.BedFinderTalent;
import doggytalents.common.talent.BlackPeltTalent;
import doggytalents.common.talent.CreeperSweeperTalent;
import doggytalents.common.talent.DoggyDashTalent;
import doggytalents.common.talent.FisherDogTalent;
import doggytalents.common.talent.GuardDogTalent;
import doggytalents.common.talent.HappyEaterTalent;
import doggytalents.common.talent.HellHoundTalent;
import doggytalents.common.talent.HunterDogTalent;
import doggytalents.common.talent.PackPuppyTalent;
import doggytalents.common.talent.PestFighterTalent;
import doggytalents.common.talent.PillowPawTalent;
import doggytalents.common.talent.PoisonFangTalent;
import doggytalents.common.talent.PuppyEyesTalent;
import doggytalents.common.talent.QuickHealerTalent;
import doggytalents.common.talent.RescueDogTalent;
import doggytalents.common.talent.RoaringGaleTalent;
import doggytalents.common.talent.ShepherdDogTalent;
import doggytalents.common.talent.SwimmerDogTalent;
import doggytalents.common.talent.WolfMountTalent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class DoggyTalents {

    public static final DeferredRegister<Talent> TALENTS = DeferredRegister.create(Talent.class, Constants.MOD_ID);

    public static final RegistryObject<BedFinderTalent> BED_FINDER = register("bed_finder", BedFinderTalent::new);
    public static final RegistryObject<BlackPeltTalent> BLACK_PELT = register("black_pelt", BlackPeltTalent::new);
    public static final RegistryObject<CreeperSweeperTalent> CREEPER_SWEEPER = register("creeper_sweeper", CreeperSweeperTalent::new);
    public static final RegistryObject<DoggyDashTalent> DOGGY_DASH = register("doggy_dash", DoggyDashTalent::new);
    public static final RegistryObject<FisherDogTalent> FISHER_DOG = register("fisher_dog", FisherDogTalent::new);
    public static final RegistryObject<GuardDogTalent> GUARD_DOG = register("guard_dog", GuardDogTalent::new);
    public static final RegistryObject<HappyEaterTalent> HAPPY_EATER = register("happy_eater", HappyEaterTalent::new);
    public static final RegistryObject<HellHoundTalent> HELL_HOUND = register("hell_hound", HellHoundTalent::new);
    public static final RegistryObject<HunterDogTalent> HUNTER_DOG = register("hunter_dog", HunterDogTalent::new);
    public static final RegistryObject<PackPuppyTalent> PACK_PUPPY = register("pack_puppy", PackPuppyTalent::new);
    public static final RegistryObject<PestFighterTalent> PEST_FIGHTER = register("pest_fighter", PestFighterTalent::new);
    public static final RegistryObject<PillowPawTalent> PILLOW_PAW = register("pillow_paw", PillowPawTalent::new);
    public static final RegistryObject<PoisonFangTalent> POISON_FANG = register("poison_fang", PoisonFangTalent::new);
    public static final RegistryObject<PuppyEyesTalent> PUPPY_EYES = register("puppy_eyes", PuppyEyesTalent::new);
    public static final RegistryObject<QuickHealerTalent> QUICK_HEALER = register("quick_healer", QuickHealerTalent::new);
    //public static final RegistryObject<RangedAttacker> RANGED_ATTACKER = register("ranged_attacker", RangedAttacker::new);
    public static final RegistryObject<RescueDogTalent> RESCUE_DOG = register("rescue_dog", RescueDogTalent::new);
    public static final RegistryObject<RoaringGaleTalent> ROARING_GALE = register("roaring_gale", RoaringGaleTalent::new);
    public static final RegistryObject<ShepherdDogTalent> SHEPHERD_DOG = register("shepherd_dog", ShepherdDogTalent::new);
    public static final RegistryObject<SwimmerDogTalent> SWIMMER_DOG = register("swimmer_dog", SwimmerDogTalent::new);
    public static final RegistryObject<WolfMountTalent> WOLF_MOUNT = register("wolf_mount", WolfMountTalent::new);

    private static <T extends Talent> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return TALENTS.register(name, sup);
    }
}