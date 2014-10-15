package doggytalents.core.helper;

import static net.minecraftforge.common.ForgeVersion.Status.*;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import doggytalents.lib.Reference;

public class Version {

	public static Status status;
	
    public static void startVersionCheck() {
        new Thread("Doggy Talents Version Check") {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dl.dropbox.com/s/41iel7ss0dwfyq4/modversions.json");
                    InputStream con = url.openStream();
                    String data = new String(ByteStreams.toByteArray(con));
                    con.close();
                    Map<String, Object> json = new Gson().fromJson(data, Map.class);

                    Map<String, String> versions = (Map<String, String>)json.get("versions");
                    
                    String rec = versions.get(MinecraftForge.MC_VERSION + "-recommended");
                    ArtifactVersion current = new DefaultArtifactVersion(Reference.MOD_VERSION);

                    if (rec != null) {
                        ArtifactVersion recommended = new DefaultArtifactVersion(rec);
                        int diff = recommended.compareTo(current);

                        if (diff == 0)
                            status = UP_TO_DATE;
                        else if (diff < 0)
                            status = AHEAD;
                        else
                            status = OUTDATED;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    status = FAILED;
                }
                FMLLog.info(status.toString());
            }
        }.start();
    }
}
