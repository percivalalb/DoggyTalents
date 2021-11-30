package doggytalents.common.entity.texture;

import com.google.common.hash.Hashing;
import doggytalents.DoggyTalents2;
import doggytalents.common.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;

public class DogTextureServer {

    public static final DogTextureServer INSTANCE = new DogTextureServer();

    /**
     * Get the dedicated servers cache location
     */
    public File getServerFolder() {
        return new File(ServerLifecycleHooks.getCurrentServer().getServerDirectory(), "dog_skins");
    }

    @Nullable
    public byte[] getCachedBytes(File baseFolder, String hash) {
        InputStream stream = getCachedStream(baseFolder, hash);
        try {
            return stream != null ? IOUtils.toByteArray(stream) : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(stream);
        }

        return null;
    }

    @Nullable
    public InputStream getCachedStream(File baseFolder, String hash) {
        File cacheFile = getCacheFile(baseFolder, hash);

        if (cacheFile.isFile() && cacheFile.exists()) {
            try {
                FileInputStream stream = new FileInputStream(cacheFile);
                DoggyTalents2.LOGGER.debug("Loaded dog texture from local cache ({})", cacheFile);
                return stream;
            } catch (FileNotFoundException e) {
                DoggyTalents2.LOGGER.debug("Failed to load dog texture from local cache ({})", cacheFile);
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getHash(byte[] targetArray) {
        return Hashing.sha1().hashBytes(targetArray).toString();
    }

    public File getCacheFile(File baseFolder, String name) {
        File subFolder = new File(baseFolder, name.length() > 2 ? name.substring(0, 2) : "xx");
        File cacheFile = new File(subFolder, name);
        return cacheFile;
    }


    public ResourceLocation getResourceLocation(String name) {
        return Util.getResource("dogskins/" + name);
    }

    /**
     * Normally used to save server side
     */
    public boolean saveTexture(File baseFolder, byte[] stream) throws IOException {
        String hash = getHash(stream);
        File cacheFile = getCacheFile(baseFolder, hash);

        if (!cacheFile.isFile()) {
            DoggyTalents2.LOGGER.debug("Saved dog texture to local cache ({})", cacheFile);
            FileUtils.writeByteArrayToFile(cacheFile, stream);
            return true;
        }

        DoggyTalents2.LOGGER.debug("Server already has cache for dog texture ({})", cacheFile);
        return false;
    }
}
