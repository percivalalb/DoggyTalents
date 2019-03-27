package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModRecipes;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.network.PacketHandler;
import doggytalents.talent.BedFinder;
import doggytalents.talent.BlackPelt;
import doggytalents.talent.CreeperSweeper;
import doggytalents.talent.DoggyDash;
import doggytalents.talent.FisherDog;
import doggytalents.talent.GuardDog;
import doggytalents.talent.HappyEater;
import doggytalents.talent.HellHound;
import doggytalents.talent.HunterDog;
import doggytalents.talent.PackPuppy;
import doggytalents.talent.PestFighter;
import doggytalents.talent.PillowPaw;
import doggytalents.talent.PoisonFang;
import doggytalents.talent.PuppyEyes;
import doggytalents.talent.QuickHealer;
import doggytalents.talent.RescueDog;
import doggytalents.talent.RoaringGale;
import doggytalents.talent.ShepherdDog;
import doggytalents.talent.SwimmerDog;
import doggytalents.talent.WolfMount;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SideProxy {
	
	public SideProxy() {
        DoggyTalentsMod.LOGGER.debug("SideProxy init");

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }

	protected void preInit(FMLCommonSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("SideProxy preInit");
        ModRecipes.registerRecipes();
        PacketHandler.register();
        
        TalentRegistry.registerTalent(new BedFinder());
		TalentRegistry.registerTalent(new BlackPelt());
		TalentRegistry.registerTalent(new CreeperSweeper());
		TalentRegistry.registerTalent(new DoggyDash());
		TalentRegistry.registerTalent(new FisherDog());
		TalentRegistry.registerTalent(new GuardDog());
		TalentRegistry.registerTalent(new HappyEater());
		TalentRegistry.registerTalent(new HellHound());
		TalentRegistry.registerTalent(new HunterDog());
		TalentRegistry.registerTalent(new PackPuppy());
		TalentRegistry.registerTalent(new PestFighter());
		TalentRegistry.registerTalent(new PillowPaw());
		TalentRegistry.registerTalent(new PoisonFang());
		TalentRegistry.registerTalent(new PuppyEyes());
		TalentRegistry.registerTalent(new QuickHealer());
		//TalentRegistry.registerTalent(new RangedAttacker()); TODO RangedAttacker
		TalentRegistry.registerTalent(new RescueDog());
		TalentRegistry.registerTalent(new RoaringGale());
		TalentRegistry.registerTalent(new ShepherdDog());
		TalentRegistry.registerTalent(new SwimmerDog());
		TalentRegistry.registerTalent(new WolfMount());
    }
    
    protected void init(InterModEnqueueEvent event) {
    	DoggyTalentsMod.LOGGER.debug("SideProxy init");
    }
    
    protected void postInit(InterModProcessEvent event) {
        DoggyTalentsMod.LOGGER.debug("SideProxy postInit");
        
		AddonManager.runRegisteredAddons();
    }
    
    public EntityPlayer getPlayerEntity() {
		return null;
	}
    
    public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {
    	
    }
}
