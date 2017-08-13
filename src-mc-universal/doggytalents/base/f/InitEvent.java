package doggytalents.base.f;

import doggytalents.base.IInitializationEvent;
import doggytalents.base.other.CustomParticleMessage;
import doggytalents.network.PacketDispatcher;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class InitEvent implements IInitializationEvent {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		PacketDispatcher.registerMessage(CustomParticleMessage.class);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
