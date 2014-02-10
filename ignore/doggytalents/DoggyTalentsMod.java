package doggytalents;

import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import doggytalents.api.*;
import doggytalents.block.BlockFoodBowl;
import doggytalents.common.*;
import doggytalents.entity.*;
import doggytalents.handlers.ConnectionHandler;
import doggytalents.handlers.EventRightClickEntity;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;
import doggytalents.packets.*;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
@Mod(modid = Properties.MOD_ID, name = Properties.MOD_NAME, version = Properties.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {Properties.PACKET_RENAME, Properties.PACKET_TALENT, Properties.PACKET_TEXTURE, Properties.PACKET_COMMAND, Properties.PACKET_OBEY}, packetHandler = PacketHandler.class)
public class DoggyTalentsMod {
	
	@Instance(value = Properties.MOD_ID)
	public static DoggyTalentsMod instance;
	@SidedProxy(clientSide = Properties.SP_CLIENT, serverSide = Properties.SP_SERVER)
    public static CommonProxy proxy;
	
    public static Item trainingTreat = new ItemTreat(Properties.trainingTreatID, "dt_trainingtreat", 20).setUnlocalizedName("dt.trainingTreat");
    public static Item superTreat = new ItemTreat(Properties.superTreatID, "dt_supertreat", 40).setUnlocalizedName("dt.superTreat");
    public static Item masterTreat = new ItemTreat(Properties.masterTreatID, "dt_mastertreat", 60).setUnlocalizedName("dt.masterTreat");
    public static Item direTreat = new ItemDireTreat(Properties.direTreatID, "dt_diretreat").setUnlocalizedName("dt.direTreat");
    public static Item breedingBone = new ItemDT(Properties.breedingBoneID, "dt_breedingbone").setUnlocalizedName("dt.breedingBone");
    public static Item throwBone = new ItemThrowBone(Properties.throwBoneID, "dt_throwbone", "dt_droolthrowbone").setUnlocalizedName("dt.throwBone").setMaxStackSize(1);
    public static Item collarShears = new ItemDT(Properties.collarShearsID, "dt_collarshears").setUnlocalizedName("dt.collarShears").setMaxDamage(16);
    public static Item commandEmblem = new ItemDT(Properties.commandEmblemID, "dt_commandemblem").setUnlocalizedName("dt.commandEmblem");
    public static Item doggyCharm = new ItemDoggyCharm(Properties.doggyCharmID, "dt_doggycharm").setUnlocalizedName("dt.doggyCharm");
    
    public static Block foodBowl = new BlockFoodBowl(Properties.foodBowlID).setUnlocalizedName("dt.foodBowl");
    
	public DoggyTalentsMod() {
   	 	instance = this;
    }
	
	@PreInit
	public void preInit(FMLPreInitializationEvent par1) {
		this.loadConfig(new Configuration(par1.getSuggestedConfigurationFile()));
		proxy.onModPre();
		NetworkRegistry.instance().registerGuiHandler(this.instance, proxy);
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		GameRegistry.registerBlock(this.foodBowl, "dogFoodBowl");
		GameRegistry.registerTileEntity(TileEntityFoodBowl.class, "dogFoodBowl");
		MinecraftForge.EVENT_BUS.register(new EventRightClickEntity());
	}

	@Init
	public void init(FMLInitializationEvent par1) {
		proxy.onModLoad();
		int dogID = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(EntityDTDoggy.class, "DTDoggy", dogID, 0, 0);
	    EntityRegistry.registerModEntity(EntityDTDoggy.class, "DTDoggy", dogID, this.instance, 60, 4, true);
	}
	
	@PostInit
	public void postInti(FMLPostInitializationEvent par1) {
		proxy.onModPost();
		this.addName(trainingTreat, "Training Treat");
		this.addName(direTreat, "Dire Treat");
		this.addName(new ItemStack(throwBone, 1, 0), "Throw Bone");
		this.addName(new ItemStack(throwBone, 1, 1), "Drool Throw Bone");
		this.addName(collarShears, "Collar Shears");
		this.addName(superTreat, "Super Treat");
		this.addName(breedingBone, "Breeding Bone");
		this.addName(masterTreat, "Master Treat");
		this.addName(commandEmblem, "Command Emblem");
		this.addName(doggyCharm, "Doggy Charm");
		this.addName(foodBowl, "Food Bowl");
		
        GameRegistry.addRecipe(new ItemStack(throwBone, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.slimeBall});
        GameRegistry.addShapelessRecipe(new ItemStack(throwBone, 1, 0), new Object[] {new ItemStack(throwBone, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(superTreat, 5), new Object[] { new ItemStack(trainingTreat, 1), new ItemStack(trainingTreat, 1), new ItemStack(trainingTreat, 1), new ItemStack(trainingTreat, 1), new ItemStack(trainingTreat, 1), new ItemStack(Item.appleGold, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(direTreat, 1), new Object[] {new ItemStack(masterTreat, 1), new ItemStack(masterTreat, 1), new ItemStack(masterTreat, 1), new ItemStack(masterTreat, 1), new ItemStack(masterTreat, 1), new ItemStack(Block.whiteStone, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(breedingBone, 2), new Object[] {new ItemStack(masterTreat, 1), new ItemStack(Item.beefCooked, 1), new ItemStack(Item.porkCooked, 1), new ItemStack(Item.chickenCooked, 1), new ItemStack(Item.fishCooked, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(masterTreat, 5), new Object[] {new ItemStack(superTreat, 1), new ItemStack(superTreat, 1), new ItemStack(superTreat, 1), new ItemStack(superTreat, 1), new ItemStack(superTreat, 1), new ItemStack(Item.diamond, 1)});
        GameRegistry.addRecipe(new ItemStack(trainingTreat, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Item.silk, 'U', Item.bone, 'V', Item.gunpowder, 'X', Item.sugar, 'Y', Item.wheat});
        GameRegistry.addRecipe(new ItemStack(collarShears, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.bone, 'Y', Item.shears});
        GameRegistry.addRecipe(new ItemStack(commandEmblem, 1), new Object[] {" X ", "XYX", " X ", 'X', Item.ingotGold, 'Y', Item.bow});
        GameRegistry.addRecipe(new ItemStack(foodBowl, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Item.ingotIron, 'Y', Item.bone});
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent par1) {
		
	}
	
	private void loadConfig(Configuration var1) {
		var1.load();
		Properties.config = var1;
		Properties.trainingTreatID = Integer.parseInt(var1.getItem("trainingTreat", 6020).getString());
		Properties.superTreatID = Integer.parseInt(var1.getItem("superTreat", 6021).getString());
		Properties.masterTreatID = Integer.parseInt(var1.getItem("masterTreat", 6022).getString());
		Properties.direTreatID = Integer.parseInt(var1.getItem("direTreat", 6023).getString());
		Properties.breedingBoneID = Integer.parseInt(var1.getItem("breedingBone", 6024).getString());
		Properties.throwBoneID = Integer.parseInt(var1.getItem("throwBone", 6025).getString());
		Properties.collarShearsID = Integer.parseInt(var1.getItem("collarShears", 6027).getString());
		Properties.commandEmblemID = Integer.parseInt(var1.getItem("commandEmblem", 6028).getString());
		Properties.doggyCharmID = Integer.parseInt(var1.getItem("doggyCharm", 6029).getString());
		
		Properties.foodBowlID = Integer.parseInt(var1.getBlock("foodBowl", 3210).getString());
		var1.addCustomCategoryComment("doggySettings", "Here you can change details about your dog.");
		Properties.allowPermaDeath = Boolean.parseBoolean(var1.get("doggySettings", "allowPermaDeath", false, "If set to false it means you dog can never die and will be in incpasitated mode. If set to true you dogs will die one there health = 0.").getString());
		Properties.tenDayPuppies = Boolean.parseBoolean(var1.get("doggySettings", "tenDayPuppies", true, "Do you pups take 10 days the grow.").getString());
		Properties.isHungerOn = Boolean.parseBoolean(var1.get("doggySettings", "isHungerOn", true).getString());
		Properties.barkRate = Integer.parseInt(var1.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slow the dogs bark. Lower the number the faster the dogs bark.").getString());
		Properties.direParticalsOff = Boolean.parseBoolean(var1.get("doggySettings", "direParticalsOff", false, "When false there will be portal particles if your dog is dire level 30, otherwise no dire particals.").getString());
		Properties.isStartingItemEnabled = Boolean.parseBoolean(var1.get("doggySettings", "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getString());
		var1.save();
	 }
	
	public void addName(Object objectToName, String name) {
		LanguageRegistry.instance().addNameForObject(objectToName, "en_US", name);
		LanguageRegistry.instance().addNameForObject(objectToName, "en_GB", name);
	}
	
	public void registerServerCommand(ICommand par1) {
	    CommandHandler var1 = (CommandHandler)MinecraftServer.getServer().getCommandManager();
	    var1.registerCommand(par1);
	}
}