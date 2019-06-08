package doggytalents.configuration;

import doggytalents.DoggyTalentsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiConfig {

	public static GuiScreen openGui(Minecraft mc, GuiScreen screen) {
		DoggyTalentsMod.LOGGER.info("" + screen);
		return screen;
	}
}
