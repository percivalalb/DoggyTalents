package doggytalents;

import doggytalents.addon.AddonManager;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import doggytalents.handler.ConfigurationHandler;
import doggytalents.handler.ConnectionHandler;
import doggytalents.handler.EntityInteractHandler;
import doggytalents.lib.Reference;
import doggytalents.network.PacketDispatcher;
import doggytalents.proxy.CommonProxy;
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
import doggytalents.talent.ShepherdDog;
import doggytalents.talent.SwimmerDog;
import doggytalents.talent.WolfMount;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, updateJSON = Reference.UPDATE_URL)
public class DoggyTalentsMod {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalentsMod instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;
	
	public static Configuration config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.loadConfig(config = new Configuration(event.getSuggestedConfigurationFile()));
		DoggyTalentsAPI.CREATIVE_TAB = new CreativeTabDoggyTalents();
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
		proxy.preInit();
		PacketDispatcher.registerPackets();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		MinecraftForge.EVENT_BUS.register(new EntityInteractHandler());
		MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.throwBone);
		DoggyTalentsAPI.BREED_WHITELIST.registerItem(ModItems.breedingBone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(Items.BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.throwBone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.trainingTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.masterTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.superTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.direTreat);
		
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 0, "minecraft:blocks/planks_oak");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 1, "minecraft:blocks/planks_spruce");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 2, "minecraft:blocks/planks_birch");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 3, "minecraft:blocks/planks_jungle");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 4, "minecraft:blocks/planks_acacia");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 5, "minecraft:blocks/planks_big_oak");
		
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 0, "minecraft:blocks/wool_colored_white");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 1, "minecraft:blocks/wool_colored_orange");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 2, "minecraft:blocks/wool_colored_magenta");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 3, "minecraft:blocks/wool_colored_light_blue");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 4, "minecraft:blocks/wool_colored_yellow");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 5, "minecraft:blocks/wool_colored_lime");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 6, "minecraft:blocks/wool_colored_pink");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 7, "minecraft:blocks/wool_colored_gray");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 8, "minecraft:blocks/wool_colored_silver");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 9, "minecraft:blocks/wool_colored_cyan");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 10, "minecraft:blocks/wool_colored_purple");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 11, "minecraft:blocks/wool_colored_blue");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 12, "minecraft:blocks/wool_colored_brown");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 13, "minecraft:blocks/wool_colored_green");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 14, "minecraft:blocks/wool_colored_red");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 15, "minecraft:blocks/wool_colored_black");
		
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
		TalentRegistry.registerTalent(new RescueDog());
		TalentRegistry.registerTalent(new ShepherdDog());
		TalentRegistry.registerTalent(new SwimmerDog());
		TalentRegistry.registerTalent(new WolfMount());
		
		GameRegistry.addRecipe(new ItemStack(ModItems.throwBone, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SLIME_BALL});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.throwBone, 1, 0), new Object[] {new ItemStack(ModItems.throwBone, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.superTreat, 5), new Object[] { new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(Items.GOLDEN_APPLE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.direTreat, 1), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(Blocks.END_STONE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.breedingBone, 2), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(Items.COOKED_BEEF, 1), new ItemStack(Items.COOKED_PORKCHOP, 1), new ItemStack(Items.COOKED_CHICKEN, 1), new ItemStack(Items.COOKED_FISH, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.masterTreat, 5), new Object[] {new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(Items.DIAMOND, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.trainingTreat, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Items.STRING, 'U', Items.BONE, 'V', Items.GUNPOWDER, 'X', Items.SUGAR, 'Y', Items.WHEAT});
        GameRegistry.addRecipe(new ItemStack(ModItems.collarShears, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SHEARS});
        GameRegistry.addRecipe(new ItemStack(ModItems.commandEmblem, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.GOLD_INGOT, 'Y', Items.BOW});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.foodBowl, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.BONE});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.dogBath, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.WATER_BUCKET});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.radioCollar, 1), new Object[] {"XX", "YX", 'X', Items.IRON_INGOT, 'Y', Items.REDSTONE});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.radar, 1), new Object[] {new ItemStack(Items.MAP, 1), new ItemStack(Items.REDSTONE, 1), new ItemStack(ModItems.radioCollar, 1)});
		
		
		AddonManager.registerAddons();
		AddonManager.runRegisteredAddons(ConfigurationHandler.configuration);
		proxy.postInit();
	}
}
