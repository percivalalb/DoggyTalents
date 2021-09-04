package doggytalents.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import doggytalents.DoggyTalents2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;

public class CachedFileTexture extends SimpleTexture {

    private File cacheFile;
    private boolean textureUploaded;

    public CachedFileTexture(ResourceLocation rlIn, File cacheFileIn) {
        super(rlIn);
        this.cacheFile = cacheFileIn;
    }

    private void setImage(NativeImage nativeImageIn) {
       Minecraft.getInstance().execute(() -> {
           this.textureUploaded = true;
           if (!RenderSystem.isOnRenderThread()) {
             RenderSystem.recordRenderCall(() -> {
                this.upload(nativeImageIn);
             });
          } else {
             this.upload(nativeImageIn);
          }
       });
    }

    private void upload(NativeImage imageIn) {
       TextureUtil.prepareImage(this.getId(), imageIn.getWidth(), imageIn.getHeight());
       imageIn.upload(0, 0, 0, true);
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        Minecraft.getInstance().execute(() -> {
            if (!this.textureUploaded) {
                NativeImage nativeimage = null;

                if (this.cacheFile.isFile() && this.cacheFile.exists()) {
                    DoggyTalents2.LOGGER.debug("Loading dog texture from local cache ({})", this.cacheFile);
                    FileInputStream fileinputstream = null;
                    try {
                        fileinputstream = new FileInputStream(this.cacheFile);
                        nativeimage = this.loadTexture(fileinputstream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(fileinputstream);
                    }
                }

                if (nativeimage != null) {
                    this.setImage(nativeimage);
                } else {
                    DoggyTalents2.LOGGER.warn("Was unable to set image ({})", this.cacheFile);
                }
            }
        });
    }


    @Nullable
    private NativeImage loadTexture(InputStream inputStreamIn) {
       NativeImage nativeimage = null;

       try {
           nativeimage = NativeImage.read(inputStreamIn);
       } catch (IOException ioexception) {
           DoggyTalents2.LOGGER.warn("Error while loading the skin texture", ioexception);
       }

       return nativeimage;
    }
}
