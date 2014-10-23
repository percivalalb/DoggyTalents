package doggytalents.core.helper;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import doggytalents.lib.Reference;

/**
 * @author ProPercivalalb
 */
public class LogHelper {

	public static FMLRelaunchLog instance = FMLRelaunchLog.log;

	private static void log(Level level, String format, Object... data) {
		FMLRelaunchLog.log(Reference.MOD_NAME, level, format, data);
	}

	public static void severe(String format, Object... data) {
	    log(Level.ERROR, format, data);
	}

	public static void warning(String format, Object... data) {
	    log(Level.WARN, format, data);
	}

	public static void info(String format, Object... data) {
	    log(Level.INFO, format, data);
	}
}
