package doggytalents.base.b;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RegistrySubscriber {

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		if(Reference.IS_DEV_ENVIR && !MinecraftForge.MC_VERSION.startsWith("1.10")) return;
		
		ModBlocks.onRegisterBlock(event.getRegistry());
	}
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		if(Reference.IS_DEV_ENVIR && !MinecraftForge.MC_VERSION.startsWith("1.10")) return;
		
		ModBlocks.onRegisterItem(event.getRegistry());
		ModItems.onRegister(event.getRegistry());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		if(Reference.IS_DEV_ENVIR && !MinecraftForge.MC_VERSION.startsWith("1.10")) return;
		
		ModBlocks.setItemModels();
		ModItems.setItemModels();
	}
}
