package doggytalents.client.entity.render;

import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.layer.LayerFactory;
import doggytalents.common.entity.DogEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollarRenderManager {

    private static final List<LayerFactory<DogEntity, DogModel<DogEntity>>> backer = new ArrayList<>();
    private static final List<LayerFactory<DogEntity, DogModel<DogEntity>>> accessoryRendererMap = Collections.synchronizedList(backer);

    /**
     * Register a renderer for a collar type
     * Call this during {@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}.
     * This method is safe to call during parallel mod loading.
     */
    public static void registerLayer(LayerFactory<DogEntity, DogModel<DogEntity>> shader) {
        accessoryRendererMap.add(shader);
    }

    @Nullable
    public static List<LayerFactory<DogEntity, DogModel<DogEntity>>> getLayers() {
        return Collections.unmodifiableList(backer);
    }
}
