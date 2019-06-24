package doggytalents.configuration;

import doggytalents.DoggyTalentsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiConfig {

    public static Screen openGui(Minecraft mc, Screen screen) {
        DoggyTalentsMod.LOGGER.info("" + screen);
        return screen;
    }
}
