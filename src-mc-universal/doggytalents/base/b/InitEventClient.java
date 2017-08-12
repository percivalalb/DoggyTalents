package doggytalents.base.b;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class InitEventClient implements IInitializationEvent {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.setItemModels();
		ModItems.setItemModels();
		MinecraftForge.EVENT_BUS.register(new ModelBakeWrapper());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
