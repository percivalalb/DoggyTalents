package doggytalents.core.handler;

import cpw.mods.fml.common.registry.LanguageRegistry;
import doggytalents.core.helper.LocalizationHelper;
import doggytalents.lang.Localizations;

/**
 * @author ProPercivalalb
 */
public class LocalizationHandler {

    /***
     * Loads in all the Localization files.
     */
    public static void loadLanguages() {
        //For every file specified, load them into the Language Registry
        for (Localizations localization : Localizations.values()) {
        	String localizationFile = Localizations.LANG_RESOURCE_LOCATION + localization.getFile();
            LanguageRegistry.instance().loadLocalization(localizationFile, LocalizationHelper.getLocaleFromFileName(localizationFile), LocalizationHelper.isXMLLanguageFile(localizationFile));
        }
    }

}
