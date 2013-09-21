package doggytalents.core.helper;

import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author ProPercivalalb
 */
public class LocalizationHelper {

    /***
     * A simple test to determine if a specified file name represents a XML file
     * or not
     * @param fileName String representing the file name of the file in question
     * @return True if the file name represents a XML file, false otherwise
     */
    public static boolean isXMLLanguageFile(String fileName) {
        return fileName.endsWith(".xml");
    }

    /***
     * Returns the language name from file name
     * @param fileName String representing the file name of the file in question
     * @return String representation of the locale snipped from the file name
     */
    public static String getLocaleFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
    }
}
