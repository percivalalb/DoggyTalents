package doggytalents.helper;

import static net.minecraftforge.common.ForgeVersion.Status.AHEAD;
import static net.minecraftforge.common.ForgeVersion.Status.FAILED;
import static net.minecraftforge.common.ForgeVersion.Status.OUTDATED;
import static net.minecraftforge.common.ForgeVersion.Status.PENDING;
import static net.minecraftforge.common.ForgeVersion.Status.UP_TO_DATE;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
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
                    URL url = new URL("https://raw.githubusercontent.com/ProPercivalalb/DoggyTalents/master/version.json");
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
                		EntityPlayer player = DoggyTalentsMod.proxy.getClientPlayer();
                		if(player != null) {
                			ChatComponentTranslation chatComponent = ChatHelper.getChatComponentTranslation("doggytalents.updatemessage", Reference.MOD_NAME, recommendedVersion, linkVersion);
                		  	chatComponent.getChatStyle().setItalic(true);
                		  	player.addChatMessage(chatComponent);
                		  	checkedVersion = true;
                		}
                	}
                }
            }
        }.start();
    }
}
