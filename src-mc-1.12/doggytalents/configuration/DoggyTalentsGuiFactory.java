package doggytalents.configuration;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * Created by NovaViper on 4/4/2017.
 */
public class DoggyTalentsGuiFactory implements IModGuiFactory {
	/**Get Examples from {@link net.minecraftforge.client.gui.ForgeGuiFactory}
	 *
	 * Properties are in {@link doggytalents.configuration.ConfigurationHandler}
	*/
	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ModGuiConfig(parentScreen);
	}
}