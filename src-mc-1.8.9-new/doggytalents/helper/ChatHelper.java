package doggytalents.helper;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class ChatHelper {

	public static ChatComponentTranslation getChatComponentTranslation(String message, Object... format) {
		return new ChatComponentTranslation(message, format);
	}
	
	public static ChatComponentText getChatComponent(String message) {
		return new ChatComponentText(message);
	}
}
