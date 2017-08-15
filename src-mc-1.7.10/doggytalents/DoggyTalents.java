package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.addon.AddonManager;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.configuration.ConfigurationHandler;
import doggytalents.creativetab.CreativeTabDogBed;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import doggytalents.inventory.RecipeDogBed;
import doggytalents.inventory.RecipeDogCape;
import doggytalents.inventory.RecipeDogCollar;
import doggytalents.lib.Reference;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY)
public class DoggyTalents {

	@Instance(value = Reference.MOD_ID)
	public static DoggyTalents INSTANCE;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy PROXY;
	
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
	
	public static CreativeTabs CREATIVE_TAB = new CreativeTabDoggyTalents();
	public static CreativeTabs CREATIVE_TAB_BED = new CreativeTabDogBed();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(new Configuration(event.getSuggestedConfigurationFile()));
		PROXY.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.THROW_BONE);
		DoggyTalentsAPI.BREED_WHITELIST.registerItem(ModItems.BREEDING_BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(Items.bone);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.THROW_BONE);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.TRAINING_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.MASTER_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.SUPER_TREAT);
		DoggyTalentsAPI.BEG_WHITELIST.registerItem(ModItems.DIRE_TREAT);
		
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 0);
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 1);
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 2);
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 3);
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 4);
		DogBedRegistry.CASINGS.registerMaterial(Blocks.planks, 5);
		
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 0);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 1);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 2);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 3);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 4);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 5);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 6);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 7);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 8);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 9);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 10);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 11);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 12);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 13);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 14);
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.wool, 15);
		
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
		
		GameRegistry.addRecipe(new ItemStack(ModItems.THROW_BONE, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.bone, 'Y', Items.slime_ball});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.THROW_BONE, 1, 0), new Object[] {new ItemStack(ModItems.THROW_BONE, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.SUPER_TREAT, 5), new Object[] { new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(Items.golden_apple, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.DIRE_TREAT, 1), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Blocks.end_stone, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.BREEDING_BONE, 2), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Items.cooked_beef, 1), new ItemStack(Items.cooked_porkchop, 1), new ItemStack(Items.cooked_chicken, 1), new ItemStack(Items.cooked_fished, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.MASTER_TREAT, 5), new Object[] {new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(Items.diamond, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.TRAINING_TREAT, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Items.string, 'U', Items.bone, 'V', Items.gunpowder, 'X', Items.sugar, 'Y', Items.wheat});
        GameRegistry.addRecipe(new ItemStack(ModItems.COLLAR_SHEARS, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.bone, 'Y', Items.shears});
        GameRegistry.addRecipe(new ItemStack(ModItems.COMMAND_EMBLEM, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.gold_ingot, 'Y', Items.bow});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.FOOD_BOWL, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.bone});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.DOG_BATH, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.water_bucket});
        GameRegistry.addRecipe(new ItemStack(ModItems.CHEW_STICK, 1), new Object[] {"SW", "WS", 'W', Items.wheat, 'S', Items.sugar});
        GameRegistry.addRecipe(new ItemStack(ModItems.WOOL_COLLAR, 1), new Object[] {"SSS", "S S", "SSS", 'S', Items.string});
        GameRegistry.addRecipe(new ItemStack(ModItems.TREAT_BAG, 1), new Object[] {"LCL", "LLL", 'L', Items.leather, 'C', ModItems.CHEW_STICK});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.CAPE, 1), new Object[] {"S S", "LWL", "WLW", 'L', Items.leather, 'S', Items.string, 'W', Blocks.wool});
        GameRegistry.addRecipe(new ItemStack(ModItems.CAPE_COLOURED, 1), new Object[] {"S S", "LLL", "LLL", 'L', Items.leather, 'S', Items.string});
        GameRegistry.addRecipe(new ItemStack(ModItems.LEATHER_JACKET, 1), new Object[] {"L L", "LWL", "LWL", 'L', Items.leather, 'W', new ItemStack(Blocks.wool)});
        GameRegistry.addRecipe(new ItemStack(ModItems.FANCY_COLLAR, 1, 1), new Object[] {"BWB", "WCW", "BSB", 'C', new ItemStack(ModItems.WOOL_COLLAR), 'B', new ItemStack(Items.dye, 1, 0), 'W', new ItemStack(Items.dye, 1, 15), 'S', new ItemStack(Items.string)});
        GameRegistry.addRecipe(new ItemStack(ModItems.FANCY_COLLAR, 1, 1), new Object[] {"WBW", "BCB", "WSW", 'C', new ItemStack(ModItems.WOOL_COLLAR), 'B', new ItemStack(Items.dye, 1, 0), 'W', new ItemStack(Items.dye, 1, 15), 'S', new ItemStack(Items.string)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.FANCY_COLLAR, 1, 2), new Object[] {new ItemStack(ModItems.WOOL_COLLAR), new ItemStack(Items.string), new ItemStack(ModItems.WOOL_COLLAR, 1, 4), new ItemStack(ModItems.WOOL_COLLAR, 1, 10), new ItemStack(ModItems.WOOL_COLLAR, 1, 11), new ItemStack(ModItems.WOOL_COLLAR, 1, 14), new ItemStack(ModItems.WOOL_COLLAR, 1, 1), new ItemStack(ModItems.WOOL_COLLAR, 1, 5)});
        GameRegistry.addRecipe(new ItemStack(ModItems.SUNGLASSES, 1), new Object[] {"S S", "GSG", 'S', Items.stick, 'G', new ItemStack(Blocks.glass_pane)});
        GameRegistry.addRecipe(new ItemStack(ModItems.TINY_BONE, 1), new Object[] {"BI", "IB", 'B', Items.bone, 'I', Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(ModItems.BIG_BONE, 1), new Object[] {"BI", "IB", "BI", 'B', Items.bone, 'I', Items.iron_ingot});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.RADIO_COLLAR, 1), new Object[] {"XX", "YX", 'X', Items.iron_ingot, 'Y', Items.redstone});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.RADAR, 1), new Object[] {new ItemStack(Items.map, 1), new ItemStack(Items.redstone, 1), new ItemStack(ModItems.RADIO_COLLAR, 1)});
	
        CraftingManager.getInstance().getRecipeList().add(new RecipeDogBed());
        CraftingManager.getInstance().getRecipeList().add(new RecipeDogCollar());
        CraftingManager.getInstance().getRecipeList().add(new RecipeDogCape());
		
        RecipeSorter.register("doggytalents:dogbed", RecipeDogBed.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register("doggytalents:dogcollar", RecipeDogCollar.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register("doggytalents:dogcape", RecipeDogCape.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        
		AddonManager.registerAddons();
		AddonManager.runRegisteredAddons(ConfigurationHandler.CONFIG);
		PROXY.postInit(event);
	}
}
