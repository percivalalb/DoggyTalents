package doggytalents.helper;

import static net.minecraftforge.common.ForgeVersion.Status.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import doggytalents.DoggyTalentsMod;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;

public class DoggyTalentsVersion {

	public static Status status = PENDING;
	public static String recommendedVersion = Reference.MOD_VERSION;
	public static String linkVersion = null;
	public static boolean checkedVersion = false;
	
    public static void startVersionCheck() {
    	if(!Constants.VERSION_CHECK)
    		return;
    	
        new Thread("Doggy Talents Version Check") {
            @Override
            public void run() {
                try {
                	String downloadLink = fetchDownloadLink(getUrlSource("http://www.mediafire.com/download/lh66wbyc9oxdc3r/version.json"));
                    URL url = new URL(downloadLink);
                    InputStream con = url.openStream();
                    String data = new String(ByteStreams.toByteArray(con));
                    
                    con.close();
                    Map<String, Object> json = new Gson().fromJson(data, Map.class);

                    Map<String, String> versions = (Map<String, String>)json.get("versions");
                    Map<String, String> links = (Map<String, String>)json.get("links");
                    
                    String rec = versions.get(MinecraftForge.MC_VERSION + "-recommended");
                    ArtifactVersion current = new DefaultArtifactVersion(Reference.MOD_VERSION);

                    if (rec != null) {
                        ArtifactVersion recommended = new DefaultArtifactVersion(rec);
                        int diff = recommended.compareTo(current);

                        if (diff == 0)
                            status = UP_TO_DATE;
                        else if (diff < 0)
                            status = AHEAD;
                        else {
                            status = OUTDATED;
                            recommendedVersion = rec;
                            linkVersion = links.get(rec);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    status = FAILED;
                }
                
                LogHelper.info("Received version data: %s", status);
                String chat = String.format("A new %s version exists %s. Get it here: %s", Reference.MOD_NAME, recommendedVersion, linkVersion);
                if(status == OUTDATED)
	                LogHelper.info(chat);
                
                
                while(!checkedVersion) {
                	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	if(status == OUTDATED) {
                		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                		if(player != null) {
                			IChatComponent link = ForgeHooks.newChatWithLinks(linkVersion);
                			link.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to download!")));
                			link.getChatStyle().setColor(EnumChatFormatting.YELLOW);
                			ChatComponentTranslation chatComponent = ChatHelper.getChatComponentTranslation("doggytalents.updatemessage", Reference.MOD_NAME, recommendedVersion);
                		  	chatComponent.getChatStyle().setItalic(true);
                		 
                		  	player.addChatMessage(chatComponent);
                		  	player.addChatMessage(link);
                		  	checkedVersion = true;
                		}
                	}
                }
            }
        }.start();
    }
    
    private static String getUrlSource(String spec) throws IOException {
        URL url = new URL(spec);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
        String inputLine;
        String total = "";
        while((inputLine = in.readLine()) != null)
           total += inputLine;
        
        in.close();

        return total;
    }
    
    private static String fetchDownloadLink(String str) {
        try {
            String regex = "(?=\\<)|(?<=\\>)";
            String data[] = str.split(regex);
            String found = "NOTFOUND";
            for (String dat : data) {
                if (dat.contains("DLP_mOnDownload(this)")) {
                    found = dat;
                    break;
                }
            }
            String wentthru = found.substring(found.indexOf("href=\"") + 6);
            wentthru = wentthru.substring(0, wentthru.indexOf("\""));
            return wentthru;
        } 
        catch(Exception e)  {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
