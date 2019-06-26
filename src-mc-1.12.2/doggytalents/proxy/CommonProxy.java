package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalents;
import doggytalents.ModEntities;
import doggytalents.ModItems;
import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.block.DogBedRegistry;
import doggytalents.configuration.ConfigurationHandler;
import doggytalents.entity.EntityDog;
import doggytalents.handler.ConfigChange;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.EntitySpawn;
import doggytalents.handler.LivingDrops;
import doggytalents.handler.MissingMappings;
import doggytalents.handler.PlayerConnection;
import doggytalents.helper.Compatibility;
import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.inventory.ContainerTreatBag;
import doggytalents.lib.GuiNames;
import doggytalents.lib.Reference;
import doggytalents.network.PacketDispatcher;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ProPercivalalb
 */
public class CommonProxy implements IGuiHandler {

    public void preInit(FMLPreInitializationEvent event) {
        ModEntities.init();
        ConfigurationHandler.init(new Configuration(event.getSuggestedConfigurationFile()));
        this.registerEventHandlers();
    }
    
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(DoggyTalents.INSTANCE, DoggyTalents.PROXY);
        PacketDispatcher.registerPackets();
    }

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.post(new BeddingRegistryEvent(DogBedRegistry.CASINGS, DogBedRegistry.BEDDINGS));
        DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.THROW_BONE, ModItems.THROW_BONE_WET);
        DoggyTalentsAPI.PACKPUPPY_BLACKLIST.registerItem(ModItems.THROW_STICK, ModItems.THROW_STICK_WET);
        DoggyTalentsAPI.BREED_WHITELIST.registerItem(ModItems.BREEDING_BONE);
        DoggyTalentsAPI.BEG_UNTAMED_WHITELIST.registerItem(ModItems.TRAINING_TREAT);
        DoggyTalentsAPI.BEG_UNTAMED_WHITELIST.registerItem(Items.BONE);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(Items.BONE);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.THROW_BONE);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.THROW_STICK);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.TRAINING_TREAT);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.MASTER_TREAT);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.SUPER_TREAT);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.DIRE_TREAT);
        DoggyTalentsAPI.BEG_TAMED_WHITELIST.registerItem(ModItems.TREAT_BAG);
        ModFixs fix = FMLCommonHandler.instance().getDataFixer().init(Reference.MOD_ID, 1);
        fix.registerFix(FixTypes.ITEM_INSTANCE, new Compatibility.ThrowBoneDataFixer());
        fix.registerFix(FixTypes.ENTITY, new Compatibility.EntityDogDataFixer());
    }
    
    protected void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new PlayerConnection());
        MinecraftForge.EVENT_BUS.register(new EntityInteract());
        MinecraftForge.EVENT_BUS.register(new LivingDrops());
        MinecraftForge.EVENT_BUS.register(new ConfigChange());
        MinecraftForge.EVENT_BUS.register(new EntitySpawn());
        MinecraftForge.EVENT_BUS.register(new MissingMappings());
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        
        if(ID == GuiNames.GUI_ID_DOGGY) {}
        else if(ID == GuiNames.GUI_ID_PACKPUPPY) {
            Entity target = player.world.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
                return null;
            EntityDog dog = (EntityDog)target;
            return new ContainerPackPuppy(player, dog);
        }
        else if(ID == GuiNames.GUI_ID_FOOD_BOWL) {
            TileEntity target = world.getTileEntity(new BlockPos(x, y, z));
            if(!(target instanceof TileEntityFoodBowl))
                return null;
            TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
            return new ContainerFoodBowl(player.inventory, foodBowl);
        }
        else if(ID == GuiNames.GUI_ID_FOOD_BAG) {
            return new ContainerTreatBag(player, x, player.inventory.getStackInSlot(x));
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
        return null;
    }
    
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
    
    public EntityPlayer getPlayerEntity() {
        return null;
    }
    
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return (IThreadListener)ctx.getServerHandler().player.server;
    }
    
    public void spawnCrit(World world, Entity entity) {}

    public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {}

}
