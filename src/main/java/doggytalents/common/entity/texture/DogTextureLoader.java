package doggytalents.common.entity.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.hash.Hashing;

import doggytalents.DoggyTalents2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class DogTextureLoader {

    /**
     * Get the dedicated servers cache location
     */
    public static File getServerFolder() {
        return new File(ServerLifecycleHooks.getCurrentServer().getDataDirectory(), "dog_skins");
    }

    @Nullable
    public static byte[] getCachedBytes(File baseFolder, String hash) {
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
    public static InputStream getCachedStream(File baseFolder, String hash) {
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

    @Deprecated
    public static String getHash(@Nonnull InputStream stream) {
        try {
            byte[] targetArray = IOUtils.toByteArray(stream);
            return getHash(targetArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getHash(byte[] targetArray) {
        return Hashing.sha1().hashBytes(targetArray).toString();
    }

    public static File getCacheFile(File baseFolder, String name) {
        File subFolder = new File(baseFolder, name.length() > 2 ? name.substring(0, 2) : "xx");
        File cacheFile = new File(subFolder, name);
        return cacheFile;
    }


    public static ResourceLocation getResourceLocation(String name) {
        return new ResourceLocation("doggytalents", "dogskins/" + name);
    }

    /**
     * Normally used to save server side
     */
    public static boolean saveTexture(File baseFolder, byte[] stream) throws IOException {
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