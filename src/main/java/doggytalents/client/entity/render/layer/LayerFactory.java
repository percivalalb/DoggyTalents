package doggytalents.client.entity.render.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface LayerFactory<T extends Entity, M extends EntityModel<T>> {

    RenderLayer<T, M> createLayer(RenderLayerParent<T, M> rendererIn, EntityRendererProvider.Context ctx);
}
