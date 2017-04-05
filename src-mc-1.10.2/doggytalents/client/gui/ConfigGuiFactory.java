package doggytalents.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by NovaViper on 4/4/2017.
 */
public class ConfigGuiFactory implements IModGuiFactory {
	/**Get Examples from {@link net.minecraftforge.client.gui.ForgeGuiFactory}
	 *
	 * Properties are in {@link doggytalents.handler.ConfigurationHandler}
	*/
	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ModGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
}