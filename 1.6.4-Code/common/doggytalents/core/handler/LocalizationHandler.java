package doggytalents.core.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;

import cpw.mods.fml.common.registry.LanguageRegistry;
import doggytalents.DoggyTalentsMod;
import doggytalents.core.helper.LocalizationHelper;
import doggytalents.core.helper.LogHelper;
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
            try {
            BufferedReader paramReader = new BufferedReader(new InputStreamReader(DoggyTalentsMod.class.getResourceAsStream(localizationFile))); 
			String line = "";
			while((line = paramReader.readLine()) != null) {
				if(!line.isEmpty() && !line.startsWith("#")) {
					String[] split = line.split("=");
					String inGameName = split[1];
					int count = 2;
					while(split.length > count) {
						inGameName += split[count] + (split.length - 1 != count ? "=" : "");
						++count;
					}
					LanguageRegistry.instance().addStringLocalization(split[0], LocalizationHelper.getLocaleFromFileName(localizationFile), inGameName);
				}
			}
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
        	//LanguageRegistry.instance().loadLocalization(localizationFile, LocalizationHelper.getLocaleFromFileName(localizationFile), LocalizationHelper.isXMLLanguageFile(localizationFile));
        }
    }

}
