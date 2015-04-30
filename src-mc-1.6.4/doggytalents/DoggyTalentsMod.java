package doggytalents;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.addon.AddonManager;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.DefaultDogBedIcon;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import doggytalents.handler.ConfigurationHandler;
import doggytalents.handler.ConnectionHandler;
import doggytalents.handler.EntityInteractHandler;
import doggytalents.helper.DoggyTalentsVersion;
import doggytalents.lib.Reference;
import doggytalents.network.NetworkManager;
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
import doggytalents.talent.WolfMount;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDENCIES)
public class DoggyTalentsMod {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalentsMod instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;
	
	public static NetworkManager NETWORK_MANAGER;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		DoggyTalentsVersion.startVersionCheck();
		DoggyTalentsAPI.CREATIVE_TAB = new CreativeTabDoggyTalents();
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		NETWORK_MANAGER = new NetworkManager();
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		MinecraftForge.EVENT_BUS.register(new EntityInteractHandler());
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.throwBone);
		DoggyTalentsAPI.BREED_WHITELIST.registerItem(ModItems.breedingBone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(Item.bone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.throwBone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.trainingTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.masterTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.superTreat);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.direTreat);
		
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 0);
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 1);
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 2);
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 3);
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 4);
		DogBedRegistry.CASINGS.registerMaterial(Block.planks, 5);
		
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 0);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 1);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 2);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 3);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 4);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 5);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 6);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 7);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 8);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 9);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 10);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 11);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 12);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 13);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 14);
		DogBedRegistry.BEDDINGS.registerMaterial(Block.cloth, 15);
		
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
		TalentRegistry.registerTalent(new WolfMount());
		
		GameRegistry.addRecipe(new ItemStack(ModItems.throwBone, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.slimeBall});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.throwBone, 1, 0), new Object[] {new ItemStack(ModItems.throwBone, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.superTreat, 5), new Object[] { new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(ModItems.trainingTreat, 1), new ItemStack(Item.appleGold, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.direTreat, 1), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(ModItems.masterTreat, 1), new ItemStack(Block.whiteStone, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.breedingBone, 2), new Object[] {new ItemStack(ModItems.masterTreat, 1), new ItemStack(Item.beefCooked, 1), new ItemStack(Item.porkCooked, 1), new ItemStack(Item.chickenCooked, 1), new ItemStack(Item.fishCooked, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.masterTreat, 5), new Object[] {new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(ModItems.superTreat, 1), new ItemStack(Item.diamond, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.trainingTreat, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Item.silk, 'U', Item.bone, 'V', Item.gunpowder, 'X', Item.sugar, 'Y', Item.wheat});
        GameRegistry.addRecipe(new ItemStack(ModItems.collarShears, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.shears});
        GameRegistry.addRecipe(new ItemStack(ModItems.commandEmblem, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.ingotGold, 'Y', Item.bow});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.foodBowl, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Item.ingotIron, 'Y', Item.bone});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.dogBath, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Item.ingotIron, 'Y', Item.bucketWater});
		
        GameRegistry.addRecipe(new ItemStack(ModItems.radioCollar, 1), new Object[] {"XX", "YX", 'X', Item.ingotIron, 'Y', Item.redstone});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.radar, 1), new Object[] {new ItemStack(Item.map, 1), new ItemStack(Item.redstone, 1), new ItemStack(ModItems.radioCollar, 1)});
		
		
		AddonManager.registerAddons();
		AddonManager.runRegisteredAddons(ConfigurationHandler.configuration);
		proxy.postInit();
	}
}
