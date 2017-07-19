package doggytalents.base.a;

import java.util.Set;

import doggytalents.configuration.ModGuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;

/**
 * 1.9.4 & 1.10.2 Code
 */
public class GuiFactory implements IModGuiFactory {
	
	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ModGuiConfig.class;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
}