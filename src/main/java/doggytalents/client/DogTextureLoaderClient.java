package doggytalents.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.texture.DogTextureLoader;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.RequestSkinPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;

public class DogTextureLoaderClient extends DogTextureLoader {

    public static Map<String, SkinRequest> SKIN_REQUEST_MAP = Maps.newConcurrentMap();

    public static enum SkinRequest {
        UNREQUESTED,
        REQUESTED,
        RECEIVED,
        FAILED;
    }

    public static File getClientFolder() {
        Minecraft mc = Minecraft.getInstance();
        SkinManager skinManager = mc.getSkinManager();
        return new File(skinManager.skinCacheDir.getParentFile(), "skins_dog");
    }

    @Nullable
    public static byte[] getResourceBytes(ResourceLocation loc) {
        InputStream stream = getResourceStream(loc);
        try {
            return stream != null ? IOUtils.toByteArray(stream) : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally  {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }

    @Nullable
    public static InputStream getResourceStream(ResourceLocation loc) {
        Minecraft mc = Minecraft.getInstance();

        IResourceManager resourceManager = mc.getResourceManager();
        try {
            IResource resource = resourceManager.getResource(loc);
            return resource.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Collection<ResourceLocation> getClient() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        Collection<ResourceLocation> resources = resourceManager.getAllResourceLocations("textures/entity/dog/custom", (fileName) -> {
            return fileName.endsWith(".png");
        });
        DoggyTalents2.LOGGER.debug("{} dog skins found", resources.size());
        return resources;
    }

    public static ResourceLocation getLoc(DogEntity dog) {
        if (dog.hasCustomSkin()) {
            String hash = dog.getSkinHash();
            ResourceLocation loc = getResourceLocation(hash);
            Minecraft mc = Minecraft.getInstance();

            IResourceManager resourceManager = mc.getResourceManager();
            Texture texture = getOrLoadTexture(getClientFolder(), hash);
            if (texture != null) {
                return loc;
            }



            DoggyTalents2.LOGGER.debug("Request");
            //if (ConfigValues.DISPLAY_OTHER_DOG_SKINS && SKIN_REQUEST_MAP.getOrDefault(hash, SkinRequest.UNREQUESTED) != SkinRequest.REQUESTED) {
                DoggyTalents2.LOGGER.debug("Request2");
                SKIN_REQUEST_MAP.put(hash, SkinRequest.REQUESTED);
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new RequestSkinPacket(hash));
            //}
        }


        return Resources.ENTITY_WOLF;
    }


    public static Texture getOrLoadTexture(File baseFolder, String hash) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();

        File cacheFile = getCacheFile(baseFolder, hash);
        ResourceLocation loc = getResourceLocation(hash);

        Texture texture = textureManager.getTexture(loc);
        if (texture == null && cacheFile.isFile() && cacheFile.exists()) {
            texture = new CachedFileTexture(loc, cacheFile);
            textureManager.loadTexture(loc, texture);
        }

        return texture;
    }

    /**
     *
     * @param baseFolder
     * @param stream Only one can be null
     * @param hash Only one can be null
     * @return
     */
    public static String saveTextureAndLoad(File baseFolder, byte[] stream) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();

        String hash = getHash(stream);

        File cacheFile = getCacheFile(baseFolder, hash);
        ResourceLocation loc = getResourceLocation(hash);

        Texture texture = textureManager.getTexture(loc);
        if (texture == null) {
            DoggyTalents2.LOGGER.debug("Saved dog texture to local cache ({})", cacheFile);
            try {
                FileUtils.writeByteArrayToFile(cacheFile, stream);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }

            DoggyTalents2.LOGGER.debug("Texture not current loaded trying to load");
            textureManager.loadTexture(loc, new CachedFileTexture(loc, cacheFile));
        }

        return hash;
    }

    public static class CachedFileTexture extends SimpleTexture {

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
           TextureUtil.prepareImage(this.getGlTextureId(), imageIn.getWidth(), imageIn.getHeight());
           imageIn.uploadTextureSub(0, 0, 0, true);
        }

        @Override
        public void loadTexture(IResourceManager manager) throws IOException {
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
                            // TODO Auto-generated catch block
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
}
