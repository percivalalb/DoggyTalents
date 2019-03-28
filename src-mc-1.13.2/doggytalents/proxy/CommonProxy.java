package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.ModRecipes;
import doggytalents.addon.AddonManager;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy {
	
	public CommonProxy() {
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
        
        DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.THROW_BONE, ModItems.THROW_BONE_WET);
        DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.THROW_STICK, ModItems.THROW_STICK_WET);
		DoggyTalentsAPI.BREED_WHITELIST.registerItem(ModItems.BREEDING_BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(Items.BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.THROW_BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.TRAINING_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.MASTER_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.SUPER_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.DIRE_TREAT);
        
        DogBedRegistry.CASINGS.registerMaterial(Blocks.OAK_PLANKS, "minecraft:block/oak_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.SPRUCE_PLANKS,"minecraft:block/spruce_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.BIRCH_PLANKS, "minecraft:block/birch_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.JUNGLE_PLANKS, "minecraft:block/jungle_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.ACACIA_PLANKS, "minecraft:block/acacia_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.DARK_OAK_PLANKS, "minecraft:block/dark_oak_planks");
		
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WHITE_WOOL, "minecraft:block/white_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.ORANGE_WOOL, "minecraft:block/orange_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.MAGENTA_WOOL, "minecraft:block/magenta_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_BLUE_WOOL, "minecraft:block/light_blue_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.YELLOW_WOOL, "minecraft:block/yellow_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIME_WOOL, "minecraft:block/lime_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PINK_WOOL, "minecraft:block/pink_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GRAY_WOOL, "minecraft:block/gray_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_GRAY_WOOL, "minecraft:block/light_gray_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.CYAN_WOOL, "minecraft:block/cyan_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PURPLE_WOOL, "minecraft:block/purple_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLUE_WOOL, "minecraft:block/blue_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BROWN_WOOL, "minecraft:block/brown_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GREEN_WOOL, "minecraft:block/green_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.RED_WOOL, "minecraft:block/red_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLACK_WOOL, "minecraft:block/black_wool");
        
		AddonManager.runRegisteredAddons();
    }
    
    public EntityPlayer getPlayerEntity() {
		return null;
	}
    
    public void spawnCrit(World world, Entity entity) {
    	
    }
    
    public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {
    	
    }
}
