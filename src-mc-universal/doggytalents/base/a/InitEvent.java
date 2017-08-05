package doggytalents.base.a;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IInitializationEvent;
import doggytalents.base.other.BuiltInRecipes;
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
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		BuiltInRecipes.init();
	}
}
