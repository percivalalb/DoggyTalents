package doggytalents.core.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import doggytalents.lib.Reference;

/**
 * @author ProPerivalalb
 */
public class VersionHelper {
	
	public VersionHelper() {}
	
	private static final String url = "https://dl.dropboxusercontent.com/s/4z4imfu3td2gu5w/VERSION.txt";
	
	/**
	 * Used to check for a Doggy Talents Mod update.
	 * @param type Whether it is coloured or blank
	 * @return If it successfully checked for an update.
	 */
	public static boolean checkVersion(UpdateType type) {
		assert type != null : "The variable type can not be equal to null";
		try {
			URL updateURL = new URL(url);
            HttpURLConnection updateConnection = (HttpURLConnection)updateURL.openConnection();
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String updateString = updateReader.readLine();
            String[] split = updateString.split(";");
            
            if(split.length >= 2) {
            	String newMCVersion = split[0];
            	String newModVersion = split[1];
            	String updateUrl = split[2];
            	
            	String oldMCVersion = Loader.instance().getMinecraftModContainer().getDisplayVersion();
            	String oldModVersion = Reference.MOD_VERSION;
            	
            	if(!oldModVersion.equals(newModVersion)) {
            		
            		String chatStr = null;
            		
            		if(type.equals(UpdateType.BLANK)) {
            			chatStr = "A new " + Reference.MOD_NAME + " version exists (" + newModVersion + ") for Minecraft " + newMCVersion + ". Get it here: " + updateUrl;
            			FMLLog.info(chatStr);
            			return true;
            		}
            		else { 
            			chatStr = "A new " + Reference.MOD_NAME + " version exists (" + newModVersion + ") for Minecraft " + newMCVersion + ". Get it here: " + updateUrl;
            			Side side = FMLCommonHandler.instance().getEffectiveSide();
            		  	if (side == Side.CLIENT) {
            		  		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
            		  		if(mc.thePlayer != null) {
            		  			ChatComponentText chatComponent = ChatHelper.getChatComponent(chatStr);
            		  			chatComponent.getChatStyle().setItalic(true);
            		  			mc.thePlayer.addChatMessage(chatComponent);
            		  			return true;
            		  		}
            		  	}
            		}
            	}
            }
		}
		catch(Exception e) {
			FMLLog.warning("Failed to Check for an update.");
		}
		return false;
	}
	
	public static enum UpdateType {
		//Uses /u00a7 to add yellow colour to chat.
		COLOURED,
		//Just plain text for the log.
		BLANK;
	}
}
