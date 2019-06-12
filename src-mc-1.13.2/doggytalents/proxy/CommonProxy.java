package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.ModRecipes;
import doggytalents.addon.AddonManager;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.LivingDrops;
import doggytalents.handler.MissingMappings;
import doggytalents.handler.PlayerConnection;
import doggytalents.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy {
	
	public CommonProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }
	
	protected void preInit(FMLCommonSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("CommonProxy preInit");
        ModRecipes.Registration.registerRecipes();
        PacketHandler.register();
    }
    
    protected void init(InterModEnqueueEvent event) {
    	DoggyTalentsMod.LOGGER.debug("CommonProxy init");
    	
    	MinecraftForge.EVENT_BUS.register(new PlayerConnection());
    	MinecraftForge.EVENT_BUS.register(new MissingMappings());
    	MinecraftForge.EVENT_BUS.register(new EntityInteract());
    	MinecraftForge.EVENT_BUS.register(new LivingDrops());
    }
    
    protected void postInit(InterModProcessEvent event) {
        DoggyTalentsMod.LOGGER.debug("CommonProxy postInit");
        
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
