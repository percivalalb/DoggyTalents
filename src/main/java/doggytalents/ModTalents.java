package doggytalents;

import doggytalents.api.inferface.Talent;
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
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

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
    //public static final Talent RANGED_ATTACKER = null;
    public static final Talent RESCUE_DOG = null;
    public static final Talent ROARING_GALE = null;
    public static final ShepherdDogTalent SHEPHERD_DOG = null;
    public static final Talent SWIMMER_DOG = null;
    public static final Talent WOLF_MOUNT = null;
    
    public static final void registerTalents(final RegistryEvent.Register<Talent> event) {
        IForgeRegistry<Talent> talentRegistry = event.getRegistry();
        
        DoggyTalentsMod.LOGGER.debug("Registering Talents");
        talentRegistry.register(new BedFinderTalent().setRegistryName(TalentNames.BED_FINDER));
        talentRegistry.register(new BlackPeltTalent().setRegistryName(TalentNames.BLACK_PELT));
        talentRegistry.register(new CreeperSweeperTalent().setRegistryName(TalentNames.CREEPER_SWEEPER));
        talentRegistry.register(new DoggyDashTalent().setRegistryName(TalentNames.DOGGY_DASH));
        talentRegistry.register(new FisherDogTalent().setRegistryName(TalentNames.FISHER_DOG));
        talentRegistry.register(new GuardDogTalent().setRegistryName(TalentNames.GUARD_DOG));
        talentRegistry.register(new HappyEaterTalent().setRegistryName(TalentNames.HAPPY_EATER));
        talentRegistry.register(new HellHoundTalent().setRegistryName(TalentNames.HELL_HOUND));
        talentRegistry.register(new HunterDogTalent().setRegistryName(TalentNames.HUNTER_DOG));
        talentRegistry.register(new PackPuppyTalent().setRegistryName(TalentNames.PACK_PUPPY));
        talentRegistry.register(new PestFighterTalent().setRegistryName(TalentNames.PEST_FIGHTER));
        talentRegistry.register(new PillowPawTalent().setRegistryName(TalentNames.PILLOW_PAW));
        talentRegistry.register(new PoisonFangTalent().setRegistryName(TalentNames.POISON_FANG));
        talentRegistry.register(new PuppyEyesTalent().setRegistryName(TalentNames.PUPPY_EYES));
        talentRegistry.register(new QuickHealerTalent().setRegistryName(TalentNames.QUICK_HEALER));
        //talentRegistry.register(new RangedAttacker().setRegistryName(TalentNames.RANGED_ATTACKER)); TODO RangedAttacker
        talentRegistry.register(new RescueDogTalent().setRegistryName(TalentNames.RESCUE_DOG));
        talentRegistry.register(new RoaringGaleTalent().setRegistryName(TalentNames.ROARING_GALE));
        talentRegistry.register(new ShepherdDogTalent().setRegistryName(TalentNames.SHEPHERD_DOG));
        talentRegistry.register(new SwimmerDogTalent().setRegistryName(TalentNames.SWIMMER_DOG));
        talentRegistry.register(new WolfMountTalent().setRegistryName(TalentNames.WOLF_MOUNT));
        DoggyTalentsMod.LOGGER.debug("Finished Registering Talents");
    }
}