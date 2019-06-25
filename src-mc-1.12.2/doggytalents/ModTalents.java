package doggytalents;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.ConfigValues;
import doggytalents.lib.Reference;
import doggytalents.lib.TalentNames;
import doggytalents.talent.BedFinderTalent;
import doggytalents.talent.BlackPeltTalent;
import doggytalents.talent.CreeperSweeperTalent;
import doggytalents.talent.DoggyDashTalent;
import doggytalents.talent.FisherDogTalent;
import doggytalents.talent.GuardDogTalent;
import doggytalents.talent.HappyEaterTalent;
import doggytalents.talent.HellHoundTalent;
import doggytalents.talent.HunterDogTalent;
import doggytalents.talent.PackPuppyTalent;
import doggytalents.talent.PestFighterTalent;
import doggytalents.talent.PillowPawTalent;
import doggytalents.talent.PoisonFangTalent;
import doggytalents.talent.PuppyEyesTalent;
import doggytalents.talent.QuickHealerTalent;
import doggytalents.talent.RescueDogTalent;
import doggytalents.talent.RoaringGaleTalent;
import doggytalents.talent.ShepherdDogTalent;
import doggytalents.talent.SwimmerDogTalent;
import doggytalents.talent.WolfMountTalent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModTalents {

    public static final Talent BED_FINDER = null;
    public static final Talent BLACK_PELT = null;
    public static final Talent CREEPER_SWEEPER = null;
    public static final Talent DOGGY_DASH = null;
    public static final Talent FISHER_DOG = null;
    public static final Talent GUARD_DOG = null;
    public static final Talent HAPPY_EATER = null;
    public static final Talent HELL_HOUND = null;
    public static final Talent HUNTER_DOG = null;
    public static final Talent PACK_PUPPY = null;
    public static final Talent PEST_FIGHTER = null;
    public static final Talent PILLOW_PAW = null;
    public static final Talent POISON_FANG = null;
    public static final Talent PUPPY_EYES = null;
    public static final Talent QUICK_HEALER = null;
    public static final Talent RANGED_ATTACKER = null;
    public static final Talent RESCUE_DOG = null;
    public static final Talent ROARING_GALE = null;
    public static final ShepherdDogTalent SHEPHERD_DOG = null;
    public static final Talent SWIMMER_DOG = null;
    public static final Talent WOLF_MOUNT = null;
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static final class Registration {

        @SubscribeEvent
        public static final void registerTalents(final RegistryEvent.Register<Talent> event) {
            IForgeRegistry<Talent> talentRegistry = event.getRegistry();
            
            DoggyTalents.LOGGER.debug("Registering Talents");
            tryRegister(talentRegistry, new BedFinderTalent().setRegistryName(TalentNames.BED_FINDER));
            tryRegister(talentRegistry, new BlackPeltTalent().setRegistryName(TalentNames.BLACK_PELT));
            tryRegister(talentRegistry, new CreeperSweeperTalent().setRegistryName(TalentNames.CREEPER_SWEEPER));
            tryRegister(talentRegistry, new DoggyDashTalent().setRegistryName(TalentNames.DOGGY_DASH));
            tryRegister(talentRegistry, new FisherDogTalent().setRegistryName(TalentNames.FISHER_DOG));
            tryRegister(talentRegistry, new GuardDogTalent().setRegistryName(TalentNames.GUARD_DOG));
            tryRegister(talentRegistry, new HappyEaterTalent().setRegistryName(TalentNames.HAPPY_EATER));
            tryRegister(talentRegistry, new HellHoundTalent().setRegistryName(TalentNames.HELL_HOUND));
            tryRegister(talentRegistry, new HunterDogTalent().setRegistryName(TalentNames.HUNTER_DOG));
            tryRegister(talentRegistry, new PackPuppyTalent().setRegistryName(TalentNames.PACK_PUPPY));
            tryRegister(talentRegistry, new PestFighterTalent().setRegistryName(TalentNames.PEST_FIGHTER));
            tryRegister(talentRegistry, new PillowPawTalent().setRegistryName(TalentNames.PILLOW_PAW));
            tryRegister(talentRegistry, new PoisonFangTalent().setRegistryName(TalentNames.POISON_FANG));
            tryRegister(talentRegistry, new PuppyEyesTalent().setRegistryName(TalentNames.PUPPY_EYES));
            tryRegister(talentRegistry, new QuickHealerTalent().setRegistryName(TalentNames.QUICK_HEALER));
            //tryRegister(talentRegistry, new RangedAttacker().setRegistryName(TalentNames.RANGED_ATTACKER)); TODO RangedAttacker
            tryRegister(talentRegistry, new RescueDogTalent().setRegistryName(TalentNames.RESCUE_DOG));
            tryRegister(talentRegistry, new RoaringGaleTalent().setRegistryName(TalentNames.ROARING_GALE));
            tryRegister(talentRegistry, new ShepherdDogTalent().setRegistryName(TalentNames.SHEPHERD_DOG));
            tryRegister(talentRegistry, new SwimmerDogTalent().setRegistryName(TalentNames.SWIMMER_DOG));
            tryRegister(talentRegistry, new WolfMountTalent().setRegistryName(TalentNames.WOLF_MOUNT));
            DoggyTalents.LOGGER.debug("Finished Registering Talents");
        }
        
        public static void tryRegister(IForgeRegistry<Talent> registry, Talent talent) {
            if(!ConfigValues.DISABLED_TALENTS.contains(talent.getRegistryName().toString()))
                registry.register(talent);
        }
    }
}