package doggytalents.base.b;

import doggytalents.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ModBlocksWrapper {

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		ModBlocks.onRegisterBlock(event.getRegistry());
	}
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		ModBlocks.onRegisterItem(event.getRegistry());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModBlocks.setItemModels();
	}
}
