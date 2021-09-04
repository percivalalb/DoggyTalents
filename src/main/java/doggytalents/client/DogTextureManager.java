package doggytalents.client;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import doggytalents.DoggyTalents2;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.texture.DogTextureServer;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.RequestSkinData;
import doggytalents.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class DogTextureManager extends SimplePreparableReloadListener<DogTextureManager.Preparations> {

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
        return new File(skinManager.skinsDirectory.getParentFile(), "skins_dog");
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

        ResourceManager resourceManager = mc.getResourceManager();
        Resource resource = resourceManager.getResource(loc);
        return resource.getInputStream();
    }

    public ResourceLocation getTexture(DogEntity dog) {
        if (dog.hasCustomSkin()) {
            String hash = dog.getSkinHash();
            return DogTextureManager.INSTANCE.getLocFromHashOrGet(hash, this::getCached);
        }

        return Resources.ENTITY_WOLF;
    }


    public AbstractTexture getOrLoadTexture(File baseFolder, String hash) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();

        File cacheFile = DogTextureServer.INSTANCE.getCacheFile(baseFolder, hash);
        ResourceLocation loc = DogTextureServer.INSTANCE.getResourceLocation(hash);

        AbstractTexture texture = textureManager.getTexture(loc);
        if (texture == null && cacheFile.isFile() && cacheFile.exists()) {
            texture = new CachedFileTexture(loc, cacheFile);
            textureManager.register(loc, texture);
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

        String hash = DogTextureServer.INSTANCE.getHash(data);

        File cacheFile = DogTextureServer.INSTANCE.getCacheFile(baseFolder, hash);
        ResourceLocation loc = DogTextureServer.INSTANCE.getResourceLocation(hash);

        AbstractTexture texture = textureManager.getTexture(loc);
        if (texture == null) {
            DoggyTalents2.LOGGER.debug("Saved dog texture to local cache ({})", cacheFile);
            FileUtils.writeByteArrayToFile(cacheFile, data);
            DoggyTalents2.LOGGER.debug("Texture not current loaded trying to load");
            textureManager.register(loc, new CachedFileTexture(loc, cacheFile));
        }

        return hash;
    }

    public ResourceLocation getCached(String hash) {
        if (!ConfigHandler.DISPLAY_OTHER_DOG_SKINS) {
            return Resources.ENTITY_WOLF;
        }

        ResourceLocation loc = DogTextureServer.INSTANCE.getResourceLocation(hash);

        AbstractTexture texture = getOrLoadTexture(getClientFolder(), hash);
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

    private synchronized void loadDogSkinResource(DogTextureManager.Preparations prep, Resource resource) {
        InputStream inputstream = null;
        try {
            inputstream = resource.getInputStream();
            String hash = DogTextureServer.INSTANCE.getHash(IOUtils.toByteArray(inputstream));
            ResourceLocation rl = resource.getLocation();

            if (prep.skinHashToLoc.containsKey(hash)) {
                DoggyTalents2.LOGGER.warn("The loaded resource packs contained a duplicate custom dog skin ({} & {})", resource.getLocation(), this.skinHashToLoc.get(hash));
            } else {
                DoggyTalents2.LOGGER.info("Found custom dog skin at {} with hash {}", rl, hash);
                prep.skinHashToLoc.put(hash, rl);
                prep.locToSkinHash.put(rl, hash);
                prep.customSkinLoc.add(rl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private void loadOverrideData(DogTextureManager.Preparations prep, List<Resource> resourcesList) {
        for (Resource iresource : resourcesList) {
            InputStream inputstream = iresource.getInputStream();
            DoggyTalents2.LOGGER.debug("Loading {}", iresource);
            try {
                this.loadLocaleData(prep, inputstream);
            } finally {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private synchronized void loadLocaleData(DogTextureManager.Preparations prep, InputStream inputStreamIn) {
        JsonElement jsonelement = GSON.fromJson(new InputStreamReader(inputStreamIn, StandardCharsets.UTF_8), JsonElement.class);
        JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "strings");

        for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
            String hash = entry.getKey();
            ResourceLocation texture = new ResourceLocation(GsonHelper.convertToString(entry.getValue(), hash));
            ResourceLocation previous = prep.skinHashToLoc.put(hash, texture);

            if (previous != null) {

            } else {

            }
            //prep.customSkinLoc.remove(texture);

            DoggyTalents2.LOGGER.info("Loaded override for {} -> {}", hash, texture);
        }
    }

    @Override
    protected DogTextureManager.Preparations prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        DogTextureManager.Preparations prep = new DogTextureManager.Preparations();

        profiler.startTick();

        Collection<ResourceLocation> resources = resourceManager.listResources("textures/entity/dog/custom", (fileName) -> {
            return fileName.endsWith(".png");
        });
        for (ResourceLocation rl : resources) {
            try {
                Resource resource = resourceManager.getResource(rl);

                if (resource == null) {
                    DoggyTalents2.LOGGER.warn("Could not get resource");
                    continue;
                }

                this.loadDogSkinResource(prep, resource);
            } catch (FileNotFoundException e) {
                ;
            } catch (Exception exception) {
                DoggyTalents2.LOGGER.warn("Skipped custom dog skin file: {} ({})", rl, exception);
            }
        }

        try {
            List<Resource> override = resourceManager.getResources(OVERRIDE_RESOURCE_LOCATION);
            this.loadOverrideData(prep, override);
        } catch (FileNotFoundException e) {
            ;
        }  catch (IOException | RuntimeException runtimeexception) {
            DoggyTalents2.LOGGER.warn("Unable to parse dog skin override data: {}", runtimeexception);
        }

        profiler.pop();
        profiler.endTick();
        return prep;
    }

    @Override
    protected void apply(DogTextureManager.Preparations prep, ResourceManager resourceManager, ProfilerFiller profiler) {
        prep.apply(this);
    }

    @Override
    public String getName() {
        return "DogTextureManager";
    }

    protected static class Preparations {

        private final Map<String, ResourceLocation> skinHashToLoc = new HashMap<>();
        private final Map<ResourceLocation, String> locToSkinHash = new HashMap<>();
        private final List<ResourceLocation> customSkinLoc = new ArrayList<>(20);

        public void apply(DogTextureManager dogTextureManager) {
            dogTextureManager.skinHashToLoc.clear();
            dogTextureManager.customSkinLoc.clear();

            dogTextureManager.skinHashToLoc.putAll(this.skinHashToLoc);
            dogTextureManager.locToSkinHash.putAll(this.locToSkinHash);
            dogTextureManager.customSkinLoc.addAll(this.customSkinLoc);
        }
    }
}
