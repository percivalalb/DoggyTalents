package doggytalents.configuration;

import doggytalents.DoggyTalentsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfig {

	public static GuiScreen openGui(Minecraft mc, GuiScreen screen) {
		DoggyTalentsMod.LOGGER.info("" + screen);
		return screen;
	}
}
