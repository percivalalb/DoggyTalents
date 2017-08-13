package doggytalents.base.c;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IInitializationEvent;
import doggytalents.base.other.BuiltInRecipes;
import doggytalents.base.other.CustomParticleMessage;
import doggytalents.network.PacketDispatcher;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class InitEvent implements IInitializationEvent {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.onRegisterBlock(null);
		ModBlocks.onRegisterItem(null);
		ModItems.onRegister(null);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		PacketDispatcher.registerMessage(CustomParticleMessage.class);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		BuiltInRecipes.init();
	}
}
