package doggytalents.core.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.FMLCommonHandler;
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
	public static boolean checkVersion(Type type) {
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
            		
            		if(type.equals(Type.BLANK)) {
            			chatStr = "A new " + Reference.MOD_NAME + " version exists (" + newModVersion + ") for Minecraft " + newMCVersion + ". Get it here: " + updateUrl;
            			LogHelper.logInfo(chatStr);
            			return true;
            		}
            		else {
            			chatStr = "A new \u00a7e" + Reference.MOD_NAME + " \u00a7fversion exists (\u00a7e" + newModVersion + ") \u00a7ffor Minecraft \u00a7e" + newMCVersion + ". \u00a7fGet it here: \u00a7e" + updateUrl;
            			Side side = FMLCommonHandler.instance().getEffectiveSide();
            		  	if (side == Side.CLIENT) {
            		  		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
            		  		if(mc.thePlayer != null) {
            		  			mc.thePlayer.addChatMessage(chatStr);
            		  			return true;
            		  		}
            		  	}
            		}
            	}
            }
		}
		catch(Exception e) {
			LogHelper.logWarning("Failed to Check for an update.");
		}
		return false;
	}
	
	public static enum Type {
		//Uses /u00a7 to add yellow colour to chat.
		COLOURED,
		//Just plain text for the log.
		BLANK;
	}
}
