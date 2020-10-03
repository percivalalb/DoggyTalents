package doggytalents.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import doggytalents.DoggyTalents2;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.texture.DogTextureServer;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.RequestSkinData;
import doggytalents.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

public class DogTextureManager extends DogTextureServer implements ISelectiveResourceReloadListener {

    public static final DogTextureManager INSTANCE = new DogTextureManager();
    private static final Gson GSON = new Gson();
    private static final ResourceLocation OVERRIDE_RESOURCE_LOCATION = Util.getResource("textures/entity/dog/custom/overrides.json");

    private final Map<String, SkinRequest> hashToSkinRequest = Maps.newConcurrentMap();

    protected final Map<String, ResourceLocation> skinHashToLoc = Maps.newHashMap();
    protected final Map<ResourceLocation, String> locToSkinHash = Maps.newHashMap();
    protected final List<ResourceLocation> customSkinLoc = new ArrayList<>(20);

    public SkinRequest getRequestStatus(String hash) {
        return this.hashToSkinRequest.getOrDefault(hash, SkinRequest.UNREQUESTED);
    }

    public void setRequestHandled(String hash) {
        this.hashToSkinRequest.put(hash, SkinRequest.RECEIVED);
    }

    public void setRequestFailed(String hash) {
        this.hashToSkinRequest.put(hash, SkinRequest.FAILED);
    }

    public void setRequested(String hash) {
        this.hashToSkinRequest.put(hash, SkinRequest.REQUESTED);
    }

    public List<ResourceLocation> getAll() {
        return Collections.unmodifiableList(this.customSkinLoc);
    }

    public ResourceLocation getLocFromHashOrGet(String hash, Function<? super String, ? extends ResourceLocation> mappingFunction) {
        return this.skinHashToLoc.computeIfAbsent(hash, mappingFunction);
    }

    public String getTextureHash(ResourceLocation loc) {
        return this.locToSkinHash.getOrDefault(loc, "MISSING_MAPPING");
    }

    public ResourceLocation getTextureLoc(String loc) {
        return this.skinHashToLoc.getOrDefault(loc, null); // TODO return missing not null
    }


    public File getClientFolder() {
        Minecraft mc = Minecraft.getInstance();
        SkinManager skinManager = mc.getSkinManager();
        return new File(skinManager.skinCacheDir.getParentFile(), "skins_dog");
    }

    @Nullable
    public byte[] getResourceBytes(ResourceLocation loc) throws IOException {
        InputStream stream = null;

        try {
            stream = this.getResourceStream(loc);
            return IOUtils.toByteArray(stream);
        } finally  {
            IOUtils.closeQuietly(stream);
        }
    }

    @Nullable
    public InputStream getResourceStream(ResourceLocation loc) throws IOException {
        Minecraft mc = Minecraft.getInstance();

        IResourceManager resourceManager = mc.getResourceManager();
        IResource resource = resourceManager.getResource(loc);
        return resource.getInputStream();
    }

    public ResourceLocation getTexture(DogEntity dog) {
        if (dog.hasCustomSkin()) {
            String hash = dog.getSkinHash();
            return DogTextureManager.INSTANCE.getLocFromHashOrGet(hash, this::getCached);
        }

        return Resources.ENTITY_WOLF;
    }


    public Texture getOrLoadTexture(File baseFolder, String hash) {
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
     * @param baseFolder The top level folder
     * @param data The data
     */
    public String saveTextureAndLoad(File baseFolder, byte[] data) throws IOException {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();

        String hash = this.getHash(data);

        File cacheFile = this.getCacheFile(baseFolder, hash);
        ResourceLocation loc = this.getResourceLocation(hash);

        Texture texture = textureManager.getTexture(loc);
        if (texture == null) {
            DoggyTalents2.LOGGER.debug("Saved dog texture to local cache ({})", cacheFile);
            FileUtils.writeByteArrayToFile(cacheFile, data);
            DoggyTalents2.LOGGER.debug("Texture not current loaded trying to load");
            textureManager.loadTexture(loc, new CachedFileTexture(loc, cacheFile));
        }

        return hash;
    }

    public ResourceLocation getCached(String hash) {
        if (!ConfigValues.DISPLAY_OTHER_DOG_SKINS) {
            return Resources.ENTITY_WOLF;
        }

        ResourceLocation loc = this.getResourceLocation(hash);

        Texture texture = getOrLoadTexture(getClientFolder(), hash);
        if (texture != null) {
            return loc;
        }

        if (!this.getRequestStatus(hash).requested()) {
            DoggyTalents2.LOGGER.debug("Marked {} dog skin to be requested from the server", hash);
            this.setRequested(hash);
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new RequestSkinData(hash));
        }

        return Resources.ENTITY_WOLF;
    }

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.TEXTURES;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(this.getResourceType())) {

            this.skinHashToLoc.clear();
            this.customSkinLoc.clear();

            Collection<ResourceLocation> resources = resourceManager.getAllResourceLocations("textures/entity/dog/custom", (fileName) -> {
                return fileName.endsWith(".png");
            });

            for (ResourceLocation rl : resources) {
                try {
                    IResource resource = resourceManager.getResource(rl);

                    if (resource == null) {
                        DoggyTalents2.LOGGER.warn("Could not get resource");
                        continue;
                    }

                    this.loadDogSkinResource(resource);
                } catch (FileNotFoundException e) {
                    ;
                } catch (Exception exception) {
                    DoggyTalents2.LOGGER.warn("Skipped custom dog skin file: {} ({})", rl, exception);
                }
            }

            try {
                List<IResource> resourcelocation = resourceManager.getAllResources(OVERRIDE_RESOURCE_LOCATION);
                this.loadOverrideData(resourcelocation);
            } catch (FileNotFoundException e) {
                ;
            }  catch (IOException | RuntimeException runtimeexception) {
                DoggyTalents2.LOGGER.warn("Unable to parse dog skin override data: {}", runtimeexception);
            }
        }
    }

    private synchronized void loadDogSkinResource(IResource resource) {
        InputStream inputstream = null;
        try {
            inputstream = resource.getInputStream();
            String hash = this.getHash(IOUtils.toByteArray(inputstream));
            ResourceLocation rl = resource.getLocation();

            if (this.skinHashToLoc.containsKey(hash)) {
                DoggyTalents2.LOGGER.warn("The loaded resource packs contained a duplicate custom dog skin ({} & {})", resource.getLocation(), this.skinHashToLoc.get(hash));
            } else {
                DoggyTalents2.LOGGER.info("Found custom dog skin at {} with hash {}", rl, hash);
                this.skinHashToLoc.put(hash, rl);
                this.locToSkinHash.put(rl, hash);
                this.customSkinLoc.add(rl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private void loadOverrideData(List<IResource> resourcesList) {
        for (IResource iresource : resourcesList) {
            InputStream inputstream = iresource.getInputStream();

            try {
                this.loadLocaleData(inputstream);
            } finally {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private synchronized void loadLocaleData(InputStream inputStreamIn) {
        JsonElement jsonelement = GSON.fromJson(new InputStreamReader(inputStreamIn, StandardCharsets.UTF_8), JsonElement.class);
        JsonObject jsonobject = JSONUtils.getJsonObject(jsonelement, "strings");

        for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
            String hash = entry.getKey();
            ResourceLocation texture = new ResourceLocation(JSONUtils.getString(entry.getValue(), hash));
            ResourceLocation previous = this.skinHashToLoc.put(hash, texture);

            if (previous != null) {

            } else {

            }
            //this.customSkinLoc.remove(texture);

            DoggyTalents2.LOGGER.info("Loaded override for {} -> {}", hash, texture);
        }
    }

}
