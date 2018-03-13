package doggytalents.base.other;

import doggytalents.ModItems;
import doggytalents.base.VersionControl.VersionConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ItemColours {

	public static void postInit(FMLPostInitializationEvent event) {

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {

			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if(stack.hasTagCompound())
					if(stack.getTagCompound().hasKey("collar_colour"))
						return stack.getTagCompound().getInteger("collar_colour");
				return -1;
			}
			
		}, ModItems.WOOL_COLLAR);
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {

		@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if(stack.hasTagCompound())
					if(stack.getTagCompound().hasKey("cape_colour"))
						return stack.getTagCompound().getInteger("cape_colour");
				return -1;
			}
			
		}, ModItems.CAPE_COLOURED);
	}
}
