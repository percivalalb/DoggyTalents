package doggytalents.helper;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class ChatUtil {

	public static TextComponentTranslation getChatComponentTranslation(String message, Object... format) {
		return new TextComponentTranslation(message, format);
	}
	
	public static TextComponentString getChatComponent(String message) {
		return new TextComponentString(message);
	}
}
