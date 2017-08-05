package doggytalents.base.a;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class InitEventClient implements IInitializationEvent {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.setItemModels();
		ModItems.setItemModels();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
