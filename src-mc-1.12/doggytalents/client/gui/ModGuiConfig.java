package doggytalents.client.gui;

import doggytalents.handler.ConfigurationHandler;
import doggytalents.lib.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NovaViper on 4/4/2017.
 */
public class ModGuiConfig extends GuiConfig {
	
	//@formatter:off
	public ModGuiConfig(GuiScreen guiScreen) {
		super(guiScreen, getConfigElements(), Reference.MOD_ID, false, false, I18n.format("title.doggytalents.config.name"));
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();
		list.add(new DummyCategoryElement("doggysettings", "modgui.config.doggysettings", DoggySettingsEntry.class));
		list.add(new DummyCategoryElement("general", "modgui.config.general", DTGeneralEntry.class));
		return list;
	}

	/**
	 * This custom list entry provides the General Settings entry on the
	 * Minecraft Forge Configuration screen. It extends the base Category
	 * entry class and defines the IConfigElement objects that will be used
	 * to build the child screen.
	 *
	 */
	public static class DoggySettingsEntry extends CategoryEntry // Function Entry
	{
		public DoggySettingsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen() {

			List<IConfigElement> list = new ArrayList<IConfigElement>();
			// list.add(new DummyCategoryElement("terrain", "gui.config.terrain", TerrainEntry.class)); You could also add in subcategories
			list.addAll((new ConfigElement(ConfigurationHandler.config.getCategory(ConfigurationHandler.CATEGORY_DOGGYSETTINGS))).getChildElements());
			return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, ConfigurationHandler.CATEGORY_DOGGYSETTINGS, this.configElement.requiresWorldRestart() ||
					this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() ||
					this.owningScreen.allRequireMcRestart, I18n.format("modgui.config.doggysettings"), I18n.format("modgui.config.doggysettings.tooltip"));
		}
	}

	public static class DTGeneralEntry extends CategoryEntry // Load Entry
	{
		public DTGeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}
		
		@Override
		protected GuiScreen buildChildScreen() {
			
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			// list.add(new DummyCategoryElement("terrain", "gui.config.terrain", TerrainEntry.class)); TODO You could also add in subcategories
			list.addAll((new ConfigElement(ConfigurationHandler.config.getCategory(ConfigurationHandler.CATEGORY_DT_GENERAL))).getChildElements());
			return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, ConfigurationHandler.CATEGORY_DT_GENERAL, this.configElement.requiresWorldRestart() ||
					this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() ||
					this.owningScreen.allRequireMcRestart, I18n.format("modgui.config.general"), I18n.format("modgui.config.general.tooltip"));
		}
	}
	
	//TODO All of this down here you can use as reference to create categories with nested categories
	/*public static class MiscEntry extends CategoryEntry // Miscellaneous Entry
	{
		public MiscEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			list.add(new DummyCategoryElement("dimensions", Strings.getGuiName(ModReference.MOD_ID, "config", "dimensions"), DimensionsEntry.class));
			list.add(new DummyCategoryElement("biomes", Strings.getGuiName(ModReference.MOD_ID, "config", "biomes"), BiomesEntry.class));
			list.addAll((new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_MISC))).getChildElements());

			return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, ConfigHandler.CATEGORY_MISC, this.configElement.requiresWorldRestart() ||
					this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() ||
					this.owningScreen.allRequireMcRestart, I18n.format(Strings.getGuiName(ModReference.MOD_ID, "config", "misc"), I18n.format(Strings.getGuiTooltip(ModReference.MOD_ID, "config", "misc"))));
		}
	}

	public static class DimensionsEntry extends CategoryEntry // Miscellaneous Entry
	{
		public DimensionsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen() {
			return new GuiConfig(this.owningScreen, (new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_DIMENSION)).getChildElements()), this.owningScreen.modID, ConfigHandler.CATEGORY_DIMENSION, this.configElement.requiresWorldRestart() ||
					this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() ||
					this.owningScreen.allRequireMcRestart, I18n.format(Strings.getGuiName(ModReference.MOD_ID, "config", "dimensions")), I18n.format(Strings.getGuiTooltip(ModReference.MOD_ID, "config", "dimensions")));
		}
	}

	public static class BiomesEntry extends CategoryEntry // Miscellaneous Entry
	{
		public BiomesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen() {

			return new GuiConfig(this.owningScreen, (new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BIOME)).getChildElements()), this.owningScreen.modID, ConfigHandler.CATEGORY_BIOME, this.configElement.requiresWorldRestart() ||
					this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() ||
					this.owningScreen.allRequireMcRestart, I18n.format(Strings.getGuiName(ModReference.MOD_ID, "config", "biomes")), I18n.format(Strings.getGuiTooltip(ModReference.MOD_ID, "config", "biomes")));
		}
	}*/
}
