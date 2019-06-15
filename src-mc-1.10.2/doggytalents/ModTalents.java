package doggytalents;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.Constants;
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
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class ModTalents {

	public static Talent BED_FINDER = null;
	public static Talent BLACK_PELT = null;
	public static Talent CREEPER_SWEEPER = null;
	public static Talent DOGGY_DASH = null;
	public static Talent FISHER_DOG = null;
	public static Talent GUARD_DOG = null;
	public static Talent HAPPY_EATER = null;
	public static Talent HELL_HOUND = null;
	public static Talent HUNTER_DOG = null;
	public static Talent PACK_PUPPY = null;
	public static Talent PEST_FIGHTER = null;
	public static Talent PILLOW_PAW = null;
	public static Talent POISON_FANG = null;
	public static Talent PUPPY_EYES = null;
	public static Talent QUICK_HEALER = null;
	public static Talent RANGED_ATTACKER = null;
	public static Talent RESCUE_DOG = null;
	public static Talent ROARING_GALE = null;
	public static ShepherdDogTalent SHEPHERD_DOG = null;
	public static Talent SWIMMER_DOG = null;
	public static Talent WOLF_MOUNT = null;
	
	@Mod.EventBusSubscriber
    public static final class Registration {

	    @SubscribeEvent
	    public static final void registerTalents(final RegistryEvent.Register<Talent> event) {
	    	IForgeRegistry<Talent> talentRegistry = event.getRegistry();
	        
	        DoggyTalents.LOGGER.info("Registering Talents");
	        tryRegister(talentRegistry, BED_FINDER = new BedFinderTalent().setRegistryName(TalentNames.BED_FINDER));
			tryRegister(talentRegistry, BLACK_PELT = new BlackPeltTalent().setRegistryName(TalentNames.BLACK_PELT));
			tryRegister(talentRegistry, CREEPER_SWEEPER = new CreeperSweeperTalent().setRegistryName(TalentNames.CREEPER_SWEEPER));
			tryRegister(talentRegistry, DOGGY_DASH = new DoggyDashTalent().setRegistryName(TalentNames.DOGGY_DASH));
			tryRegister(talentRegistry, FISHER_DOG = new FisherDogTalent().setRegistryName(TalentNames.FISHER_DOG));
			tryRegister(talentRegistry, GUARD_DOG = new GuardDogTalent().setRegistryName(TalentNames.GUARD_DOG));
			tryRegister(talentRegistry, HAPPY_EATER = new HappyEaterTalent().setRegistryName(TalentNames.HAPPY_EATER));
			tryRegister(talentRegistry, HELL_HOUND = new HellHoundTalent().setRegistryName(TalentNames.HELL_HOUND));
			tryRegister(talentRegistry, HUNTER_DOG = new HunterDogTalent().setRegistryName(TalentNames.HUNTER_DOG));
			tryRegister(talentRegistry, PACK_PUPPY = new PackPuppyTalent().setRegistryName(TalentNames.PACK_PUPPY));
			tryRegister(talentRegistry, PEST_FIGHTER = new PestFighterTalent().setRegistryName(TalentNames.PEST_FIGHTER));
			tryRegister(talentRegistry, PILLOW_PAW = new PillowPawTalent().setRegistryName(TalentNames.PILLOW_PAW));
			tryRegister(talentRegistry, POISON_FANG = new PoisonFangTalent().setRegistryName(TalentNames.POISON_FANG));
			tryRegister(talentRegistry, PUPPY_EYES = new PuppyEyesTalent().setRegistryName(TalentNames.PUPPY_EYES));
			tryRegister(talentRegistry, QUICK_HEALER = new QuickHealerTalent().setRegistryName(TalentNames.QUICK_HEALER));
			//tryRegister(talentRegistry, new RangedAttacker().setRegistryName(TalentNames.RANGED_ATTACKER)); TODO RangedAttacker
			tryRegister(talentRegistry, RESCUE_DOG = new RescueDogTalent().setRegistryName(TalentNames.RESCUE_DOG));
			tryRegister(talentRegistry, ROARING_GALE = new RoaringGaleTalent().setRegistryName(TalentNames.ROARING_GALE));
			tryRegister(talentRegistry, SHEPHERD_DOG = (ShepherdDogTalent)new ShepherdDogTalent().setRegistryName(TalentNames.SHEPHERD_DOG));
			tryRegister(talentRegistry, SWIMMER_DOG = new SwimmerDogTalent().setRegistryName(TalentNames.SWIMMER_DOG));
			tryRegister(talentRegistry, WOLF_MOUNT = new WolfMountTalent().setRegistryName(TalentNames.WOLF_MOUNT));
			DoggyTalents.LOGGER.info("Finished Registering Talents");
	    }
	    
	    public static void tryRegister(IForgeRegistry<Talent> registry, Talent talent) {
	    	if(!Constants.DISABLED_TALENTS.contains(talent.getRegistryName().toString()))
	    		registry.register(talent);
	    }
    }
}