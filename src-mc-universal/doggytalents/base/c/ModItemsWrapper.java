package doggytalents.base.c;

import doggytalents.ModItems;
import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class ModItemsWrapper {

	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<Item> event) {
		ModItems.onRegister(event.getRegistry());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModItems.setItemModels();
	}
}
