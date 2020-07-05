package doggytalents.client;

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

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import doggytalents.DoggyTalents2;
import doggytalents.common.util.Util;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

public class DefaultDogTextures implements ISelectiveResourceReloadListener {

    public static final DefaultDogTextures TEXTURE = new DefaultDogTextures();
    private static final Gson GSON = new Gson();
    private static final ResourceLocation OVERRIDE_RESOURCE_LOCATION = Util.getResource("textures/entity/dog/custom/overrides.json");

    protected final Map<String, ResourceLocation> skinHashToLoc = Maps.newHashMap();
    protected final Map<ResourceLocation, String> locToSkinHash = Maps.newHashMap();
    protected final List<ResourceLocation> customSkinLoc = new ArrayList<>(20);

    public List<ResourceLocation> getAll() {
        return Collections.unmodifiableList(this.customSkinLoc);
    }

    public ResourceLocation getLocFromHashOrGet(String hash, Function<? super String, ? extends ResourceLocation> mappingFunction) {
        return this.skinHashToLoc.computeIfAbsent(hash, mappingFunction);
    }

    public String getTextureHash(ResourceLocation loc) {
        return this.locToSkinHash.getOrDefault(loc, "MISSING_MAPPING");
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
            String hash = DogTextureLoaderClient.getHash(IOUtils.toByteArray(inputstream));
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
            this.skinHashToLoc.put(hash, texture);
            this.customSkinLoc.remove(texture);

            DoggyTalents2.LOGGER.warn("Loaded override for {} -> {}", hash, texture);
        }

    }

}
